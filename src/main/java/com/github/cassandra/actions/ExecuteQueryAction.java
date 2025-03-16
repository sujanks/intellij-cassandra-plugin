package com.github.cassandra.actions;

import com.github.cassandra.settings.CassandraSettingsState;
import com.github.cassandra.toolwindow.CqlQueryPanel;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExecuteQueryAction extends AnAction {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages.CassandraBundle");

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;

        CqlQueryPanel queryPanel = (CqlQueryPanel) e.getDataContext().getData("CqlQueryPanel");
        if (queryPanel == null) return;

        String query = editor.getDocument().getText();
        if (query.trim().isEmpty()) {
            NotificationGroupManager.getInstance()
                    .getNotificationGroup("Cassandra CQL")
                    .createNotification(BUNDLE.getString("toolwindow.query.empty"), NotificationType.WARNING)
                    .notify(project);
            return;
        }

        // Clear previous results
        queryPanel.clearResults();

        // Execute query
        CassandraSettingsState settings = CassandraSettingsState.getInstance();
        try (com.datastax.oss.driver.api.core.CqlSession session = com.datastax.oss.driver.api.core.CqlSession.builder()
                .addContactPoint(new InetSocketAddress(settings.hosts, settings.port))
                .withAuthCredentials(settings.username, settings.password)
                .withKeyspace(settings.keyspace)
                .withLocalDatacenter(settings.datacenter)
                .build()) {

            com.datastax.oss.driver.api.core.cql.ResultSet resultSet = session.execute(query);
            
            // Get column names
            List<String> columnNames = new ArrayList<>();
            if (resultSet.getColumnDefinitions() != null) {
                resultSet.getColumnDefinitions().forEach(def -> columnNames.add(def.getName().toString()));
            }

            // Get rows
            List<List<String>> rows = new ArrayList<>();
            for (com.datastax.oss.driver.api.core.cql.Row row : resultSet) {
                List<String> rowData = new ArrayList<>();
                for (int i = 0; i < columnNames.size(); i++) {
                    Object value = row.getObject(i);
                    rowData.add(value != null ? value.toString() : "null");
                }
                rows.add(rowData);
            }

            // Log for debugging
            System.out.println("Query executed successfully. Columns: " + columnNames.size() + ", Rows: " + rows.size());

            // Update the results table
            queryPanel.setQueryResults(columnNames, rows);

            NotificationGroupManager.getInstance()
                    .getNotificationGroup("Cassandra CQL")
                    .createNotification(BUNDLE.getString("toolwindow.query.success"), NotificationType.INFORMATION)
                    .notify(project);

        } catch (Exception ex) {
            ex.printStackTrace(); // Add stack trace for debugging
            NotificationGroupManager.getInstance()
                    .getNotificationGroup("Cassandra CQL")
                    .createNotification(BUNDLE.getString("toolwindow.query.failed").replace("{0}", ex.getMessage()), NotificationType.ERROR)
                    .notify(project);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        e.getPresentation().setEnabledAndVisible(
                project != null && 
                editor != null);
    }
} 