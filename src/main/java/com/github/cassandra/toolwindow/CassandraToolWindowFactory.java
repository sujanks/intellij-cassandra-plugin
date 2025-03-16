package com.github.cassandra.toolwindow;

import com.github.cassandra.settings.CassandraSettingsState;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ResourceBundle;

public class CassandraToolWindowFactory implements ToolWindowFactory {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages.CassandraBundle");
    private CqlQueryPanel queryPanel;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // Create tabs
        JBTabs tabs = new JBTabsImpl(project);
        
        // Connection tab
        JBPanel<?> connectionPanel = createConnectionPanel(project);
        TabInfo connectionTab = new TabInfo(new JBScrollPane(connectionPanel));
        connectionTab.setText(BUNDLE.getString("toolwindow.tab.connection"));
        tabs.addTab(connectionTab);
        
        // Query tab
        queryPanel = new CqlQueryPanel(project);
        TabInfo queryTab = new TabInfo(queryPanel);
        queryTab.setText(BUNDLE.getString("toolwindow.tab.query"));
        tabs.addTab(queryTab);

        // Add the content to the tool window
        Content content = ContentFactory.getInstance().createContent(tabs.getComponent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private JBPanel<?> createConnectionPanel(Project project) {
        CassandraSettingsState settings = CassandraSettingsState.getInstance();
        
        // Create the main panel with padding
        JBPanel<?> mainPanel = new JBPanel<>(new GridBagLayout());
        mainPanel.setBackground(new Color(243, 243, 243));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;

        // Create a section panel for connection settings
        JPanel sectionPanel = new JPanel(new GridBagLayout());
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Add title
        JBLabel titleLabel = new JBLabel(BUNDLE.getString("toolwindow.content.connection"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setForeground(new Color(240, 240, 240));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        sectionPanel.add(titleLabel, c);
        
        // Reset gridwidth
        c.gridwidth = 1;
        
        // Create fonts
        Font labelFont = titleLabel.getFont().deriveFont(Font.BOLD, 12f);
        Font fieldFont = titleLabel.getFont().deriveFont(12f);
        
        // Create field style
        Color fieldBackground = Color.WHITE;
        Border fieldBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210)),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        );
        
        // Add connection settings fields
        c.gridy++;
        JBLabel hostsLabel = new JBLabel(BUNDLE.getString("toolwindow.label.hosts"));
        hostsLabel.setFont(labelFont);
        hostsLabel.setForeground(new Color(240, 240, 240));
        sectionPanel.add(hostsLabel, c);
        
        c.gridx = 1;
        c.weightx = 1.0;
        JBTextField hostsField = new JBTextField(settings.hosts);
        hostsField.setFont(fieldFont);
        hostsField.setBackground(fieldBackground);
        hostsField.setForeground(new Color(30, 30, 30));
        hostsField.setBorder(fieldBorder);
        sectionPanel.add(hostsField, c);
        
        c.gridx = 0;
        c.gridy++;
        c.weightx = 0.0;
        JBLabel portLabel = new JBLabel(BUNDLE.getString("toolwindow.label.port"));
        portLabel.setFont(labelFont);
        portLabel.setForeground(new Color(240, 240, 240));
        sectionPanel.add(portLabel, c);
        
        c.gridx = 1;
        c.weightx = 1.0;
        JBTextField portField = new JBTextField(String.valueOf(settings.port));
        portField.setFont(fieldFont);
        portField.setBackground(fieldBackground);
        portField.setForeground(new Color(30, 30, 30));
        portField.setBorder(fieldBorder);
        sectionPanel.add(portField, c);
        
        c.gridx = 0;
        c.gridy++;
        c.weightx = 0.0;
        JBLabel usernameLabel = new JBLabel(BUNDLE.getString("toolwindow.label.username"));
        usernameLabel.setFont(labelFont);
        usernameLabel.setForeground(new Color(240, 240, 240));
        sectionPanel.add(usernameLabel, c);
        
        c.gridx = 1;
        c.weightx = 1.0;
        JBTextField usernameField = new JBTextField(settings.username);
        usernameField.setFont(fieldFont);
        usernameField.setBackground(fieldBackground);
        usernameField.setForeground(new Color(30, 30, 30));
        usernameField.setBorder(fieldBorder);
        sectionPanel.add(usernameField, c);
        
        c.gridx = 0;
        c.gridy++;
        c.weightx = 0.0;
        JBLabel passwordLabel = new JBLabel(BUNDLE.getString("toolwindow.label.password"));
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(new Color(240, 240, 240));
        sectionPanel.add(passwordLabel, c);
        
        c.gridx = 1;
        c.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField(settings.password);
        passwordField.setFont(fieldFont);
        passwordField.setBackground(fieldBackground);
        passwordField.setForeground(new Color(30, 30, 30));
        passwordField.setBorder(fieldBorder);
        sectionPanel.add(passwordField, c);
        
        c.gridx = 0;
        c.gridy++;
        c.weightx = 0.0;
        JBLabel keyspaceLabel = new JBLabel(BUNDLE.getString("toolwindow.label.keyspace"));
        keyspaceLabel.setFont(labelFont);
        keyspaceLabel.setForeground(new Color(240, 240, 240));
        sectionPanel.add(keyspaceLabel, c);
        
        c.gridx = 1;
        c.weightx = 1.0;
        JBTextField keyspaceField = new JBTextField(settings.keyspace);
        keyspaceField.setFont(fieldFont);
        keyspaceField.setBackground(fieldBackground);
        keyspaceField.setForeground(new Color(30, 30, 30));
        keyspaceField.setBorder(fieldBorder);
        sectionPanel.add(keyspaceField, c);
        
        c.gridx = 0;
        c.gridy++;
        c.weightx = 0.0;
        JBLabel datacenterLabel = new JBLabel(BUNDLE.getString("toolwindow.label.datacenter"));
        datacenterLabel.setFont(labelFont);
        datacenterLabel.setForeground(new Color(240, 240, 240));
        sectionPanel.add(datacenterLabel, c);
        
        c.gridx = 1;
        c.weightx = 1.0;
        JBTextField datacenterField = new JBTextField(settings.datacenter);
        datacenterField.setFont(fieldFont);
        datacenterField.setBackground(fieldBackground);
        datacenterField.setForeground(new Color(30, 30, 30));
        datacenterField.setBorder(fieldBorder);
        sectionPanel.add(datacenterField, c);
        
        // Button panel with more padding
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Add save button with custom styling
        JButton saveButton = new JButton(BUNDLE.getString("toolwindow.button.save"));
        saveButton.setFont(labelFont);
        saveButton.setPreferredSize(new Dimension(120, 32));
        saveButton.setBackground(new Color(59, 115, 175));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(true);
        saveButton.setContentAreaFilled(true);
        saveButton.setOpaque(true);
        saveButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(48, 93, 142), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Add hover effect
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            private final Color defaultColor = new Color(59, 115, 175);
            private final Color hoverColor = new Color(71, 138, 210);
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(hoverColor);
                saveButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(58, 112, 171), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(defaultColor);
                saveButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(48, 93, 142), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                saveButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        saveButton.addActionListener(e -> {
            settings.hosts = hostsField.getText();
            try {
                settings.port = Integer.parseInt(portField.getText());
            } catch (NumberFormatException ex) {
                settings.port = 9042; // Default port
            }
            settings.username = usernameField.getText();
            settings.password = new String(passwordField.getPassword());
            settings.keyspace = keyspaceField.getText();
            settings.datacenter = datacenterField.getText();
            
            NotificationGroupManager.getInstance()
                    .getNotificationGroup("Cassandra CQL")
                    .createNotification(BUNDLE.getString("toolwindow.message.saved"), NotificationType.INFORMATION)
                    .notify(project);
        });
        buttonPanel.add(saveButton);

        // Add test connection button with custom styling
        JButton testButton = new JButton(BUNDLE.getString("toolwindow.button.test"));
        testButton.setFont(labelFont);
        testButton.setPreferredSize(new Dimension(120, 32));
        testButton.setBackground(new Color(67, 181, 129));
        testButton.setForeground(Color.WHITE);
        testButton.setFocusPainted(false);
        testButton.setBorderPainted(true);
        testButton.setContentAreaFilled(true);
        testButton.setOpaque(true);
        testButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(54, 145, 103), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Add hover effect
        testButton.addMouseListener(new java.awt.event.MouseAdapter() {
            private final Color defaultColor = new Color(67, 181, 129);
            private final Color hoverColor = new Color(80, 217, 155);
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                testButton.setBackground(hoverColor);
                testButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(65, 174, 124), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                testButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                testButton.setBackground(defaultColor);
                testButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(54, 145, 103), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                testButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        testButton.addActionListener(e -> {
            try {
                com.datastax.oss.driver.api.core.CqlSession session = com.datastax.oss.driver.api.core.CqlSession.builder()
                    .addContactPoint(new java.net.InetSocketAddress(hostsField.getText(), Integer.parseInt(portField.getText())))
                    .withAuthCredentials(usernameField.getText(), new String(passwordField.getPassword()))
                    .withKeyspace(keyspaceField.getText())
                    .withLocalDatacenter(datacenterField.getText())
                    .build();
                
                session.close();
                
                NotificationGroupManager.getInstance()
                    .getNotificationGroup("Cassandra CQL")
                    .createNotification(BUNDLE.getString("toolwindow.connection.success"), NotificationType.INFORMATION)
                    .notify(project);
            } catch (Exception ex) {
                NotificationGroupManager.getInstance()
                    .getNotificationGroup("Cassandra CQL")
                    .createNotification(BUNDLE.getString("toolwindow.connection.failed").replace("{0}", ex.getMessage()), NotificationType.ERROR)
                    .notify(project);
            }
        });
        buttonPanel.add(testButton);

        // Add button panel
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        c.weightx = 1.0;
        sectionPanel.add(buttonPanel, c);

        // Add section panel to main panel
        GridBagConstraints mc = new GridBagConstraints();
        mc.gridx = 0;
        mc.gridy = 0;
        mc.weightx = 1.0;
        mc.fill = GridBagConstraints.HORIZONTAL;
        mc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(sectionPanel, mc);

        // Add empty panel to push everything up
        mc.gridy = 1;
        mc.weighty = 1.0;
        mc.fill = GridBagConstraints.BOTH;
        JPanel spacer = new JPanel();
        spacer.setBackground(new Color(243, 243, 243));
        mainPanel.add(spacer, mc);

        return mainPanel;
    }

    public void dispose() {
        if (queryPanel != null) {
            queryPanel.dispose();
            queryPanel = null;
        }
    }
} 