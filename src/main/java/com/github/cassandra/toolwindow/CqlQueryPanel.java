package com.github.cassandra.toolwindow;

import com.github.cassandra.actions.ExecuteQueryAction;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ResourceBundle;

public class CqlQueryPanel extends JBPanel<CqlQueryPanel> {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages.CassandraBundle");
    private final Editor editor;
    private final Project project;
    private final JBTable resultsTable;
    private final DefaultTableModel tableModel;
    private final JSplitPane splitPane;
    private final JPanel resultsPanel;
    private JPanel loadingPanel;
    private Timer loadingDotsTimer;
    private int loadingDots = 0;
    private JLabel loadingLabel;

    public CqlQueryPanel(@NotNull Project project) {
        super(new BorderLayout());
        this.project = project;
        setBackground(new Color(243, 243, 243));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Create loading animation timer
        loadingDotsTimer = new Timer(500, e -> {
            loadingDots = (loadingDots + 1) % 4;
            StringBuilder dots = new StringBuilder();
            for (int i = 0; i < loadingDots; i++) {
                dots.append(".");
            }
            if (loadingLabel != null) {
                loadingLabel.setText("Loading" + dots.toString());
            }
        });

        // Create top panel for editor
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Add title label
        JLabel titleLabel = new JLabel(BUNDLE.getString("toolwindow.content.query"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Create editor panel
        JPanel editorPanel = new JPanel(new BorderLayout());
        editorPanel.setBackground(Color.WHITE);
        editorPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Create editor
        EditorFactory editorFactory = EditorFactory.getInstance();
        Document document = editorFactory.createDocument("");
        editor = editorFactory.createEditor(document, project, com.github.cassandra.CqlFileType.INSTANCE, false);

        // Configure editor
        if (editor instanceof EditorEx) {
            EditorEx editorEx = (EditorEx) editor;
            editorEx.setHighlighter(EditorHighlighterFactory.getInstance().createEditorHighlighter(project,
                    com.github.cassandra.CqlFileType.INSTANCE));
            editorEx.setPlaceholder(BUNDLE.getString("toolwindow.query.placeholder"));
            editorEx.getSettings().setUseSoftWraps(true);
            editorEx.getSettings().setAdditionalLinesCount(1);
            editorEx.getSettings().setAdditionalColumnsCount(1);
            editorEx.getSettings().setLineNumbersShown(true);
            editorEx.getSettings().setFoldingOutlineShown(true);
        }

        editorPanel.add(editor.getComponent(), BorderLayout.CENTER);
        editorPanel.setPreferredSize(new Dimension(-1, 400));
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton executeButton = new JButton(BUNDLE.getString("toolwindow.button.execute"));
        executeButton.setFont(executeButton.getFont().deriveFont(Font.BOLD, 12f));
        executeButton.setPreferredSize(new Dimension(120, 32));
        executeButton.setBackground(new Color(59, 115, 175));
        executeButton.setForeground(Color.WHITE);
        executeButton.setFocusPainted(false);
        executeButton.setBorderPainted(true);
        executeButton.setContentAreaFilled(true);
        executeButton.setOpaque(true);
        executeButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(48, 93, 142), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Add hover effect with transition
        executeButton.putClientProperty("JButton.backgroundColor", new Color(59, 115, 175));
        executeButton.putClientProperty("JButton.borderColor", new Color(48, 93, 142));
        
        Timer transitionTimer = new Timer(20, null);
        final int[] transitionProgress = {0};
        
        executeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            private final Color defaultColor = new Color(59, 115, 175);
            private final Color hoverColor = new Color(82, 145, 212);
            private final Color defaultBorderColor = new Color(48, 93, 142);
            private final Color hoverBorderColor = new Color(71, 138, 210);
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                transitionTimer.stop();
                transitionProgress[0] = 0;
                
                transitionTimer.addActionListener(e -> {
                    transitionProgress[0] += 10;
                    if (transitionProgress[0] >= 100) {
                        transitionProgress[0] = 100;
                        transitionTimer.stop();
                    }
                    
                    float ratio = transitionProgress[0] / 100f;
                    Color currentColor = interpolateColor(defaultColor, hoverColor, ratio);
                    Color currentBorderColor = interpolateColor(defaultBorderColor, hoverBorderColor, ratio);
                    
                    executeButton.setBackground(currentColor);
                    executeButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(currentBorderColor, 1),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)
                    ));
                });
                
                transitionTimer.start();
                executeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                transitionTimer.stop();
                transitionProgress[0] = 0;
                
                transitionTimer.addActionListener(e -> {
                    transitionProgress[0] += 10;
                    if (transitionProgress[0] >= 100) {
                        transitionProgress[0] = 100;
                        transitionTimer.stop();
                    }
                    
                    float ratio = transitionProgress[0] / 100f;
                    Color currentColor = interpolateColor(hoverColor, defaultColor, ratio);
                    Color currentBorderColor = interpolateColor(hoverBorderColor, defaultBorderColor, ratio);
                    
                    executeButton.setBackground(currentColor);
                    executeButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(currentBorderColor, 1),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)
                    ));
                });
                
                transitionTimer.start();
                executeButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            private Color interpolateColor(Color c1, Color c2, float ratio) {
                int red = (int) (c1.getRed() * (1 - ratio) + c2.getRed() * ratio);
                int green = (int) (c1.getGreen() * (1 - ratio) + c2.getGreen() * ratio);
                int blue = (int) (c1.getBlue() * (1 - ratio) + c2.getBlue() * ratio);
                return new Color(red, green, blue);
            }
        });
        
        executeButton.addActionListener(e -> {
            ExecuteQueryAction action = new ExecuteQueryAction();
            DataContext dataContext = new DataContext() {
                @Override
                public Object getData(@NotNull String dataId) {
                    if (CommonDataKeys.PROJECT.is(dataId)) {
                        return project;
                    }
                    if (CommonDataKeys.EDITOR.is(dataId)) {
                        return editor;
                    }
                    if ("CqlQueryPanel".equals(dataId)) {
                        return CqlQueryPanel.this;
                    }
                    return null;
                }
            };
            AnActionEvent event = AnActionEvent.createFromDataContext(ActionPlaces.UNKNOWN, null, dataContext);
            
            // Show loading before executing the query
            showLoading();
            
            // Execute query in background
            ApplicationManager.getApplication().executeOnPooledThread(() -> {
                try {
                    action.actionPerformed(event);
                } finally {
                    // Hide loading when query completes (whether successful or not)
                    hideLoading();
                }
            });
        });
        buttonPanel.add(executeButton);

        // Add components to top panel
        JPanel editorWithButton = new JPanel(new BorderLayout());
        editorWithButton.setBackground(Color.WHITE);
        editorWithButton.add(editorPanel, BorderLayout.CENTER);
        editorWithButton.add(buttonPanel, BorderLayout.SOUTH);
        topPanel.add(editorWithButton, BorderLayout.CENTER);

        // Create results panel
        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(Color.WHITE);
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Add results title
        JLabel resultsTitle = new JLabel(BUNDLE.getString("toolwindow.results.title"));
        resultsTitle.setFont(resultsTitle.getFont().deriveFont(Font.BOLD, 16f));
        resultsTitle.setForeground(new Color(60, 60, 60));
        resultsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        resultsPanel.add(resultsTitle, BorderLayout.NORTH);

        // Create results table with non-editable model
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultsTable = new JBTable(tableModel);
        resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resultsTable.getTableHeader().setReorderingAllowed(false);
        resultsTable.setShowGrid(true);
        resultsTable.setGridColor(new Color(220, 220, 220));
        resultsTable.setRowHeight(32);
        resultsTable.setFont(resultsTable.getFont().deriveFont(12f));
        resultsTable.setBackground(Color.WHITE);
        resultsTable.setForeground(new Color(30, 30, 30));
        resultsTable.setSelectionBackground(new Color(232, 242, 254));
        resultsTable.setSelectionForeground(new Color(30, 30, 30));
        resultsTable.setIntercellSpacing(new Dimension(1, 1));
        
        // Style the header
        JTableHeader header = resultsTable.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD, 13f));
        header.setPreferredSize(new Dimension(-1, 36));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(30, 30, 30));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        
        // Custom header renderer for better styling
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                         boolean isSelected, boolean hasFocus,
                                                         int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));
                label.setBackground(new Color(240, 240, 240));
                label.setForeground(new Color(30, 30, 30));
                label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                return label;
            }
        });

        // Custom cell renderer for data cells
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                         boolean isSelected, boolean hasFocus,
                                                         int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 250, 250));
                    c.setForeground(new Color(30, 30, 30));
                }
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return c;
            }
        };
        cellRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        
        // Apply the cell renderer to all columns
        for (int i = 0; i < resultsTable.getColumnCount(); i++) {
            resultsTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        JBScrollPane tableScrollPane = new JBScrollPane(resultsTable);
        tableScrollPane.setBackground(Color.WHITE);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        tableScrollPane.setPreferredSize(new Dimension(-1, 300));
        resultsPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Create split pane
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(resultsPanel);
        splitPane.setResizeWeight(0.3);
        splitPane.setDividerLocation(250);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        splitPane.setBackground(new Color(243, 243, 243));
        splitPane.setDividerSize(6);
        
        // Add split pane to panel
        add(splitPane, BorderLayout.CENTER);

        // Initialize the loading panel after adding to hierarchy
        addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                if (getRootPane() != null && loadingPanel == null) {
                    initializeLoadingPanel();
                }
            }

            @Override
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}

            @Override
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });
    }

    private void initializeLoadingPanel() {
        loadingPanel = new JPanel(new GridBagLayout());
        loadingPanel.setBackground(new Color(0, 0, 0, 100));
        loadingPanel.setVisible(false);
        loadingPanel.setOpaque(false);
        
        loadingLabel = new JLabel("Loading");
        loadingLabel.setFont(loadingLabel.getFont().deriveFont(Font.BOLD, 16f));
        loadingLabel.setForeground(Color.WHITE);
        loadingPanel.add(loadingLabel);

        getRootPane().setGlassPane(loadingPanel);
    }

    public void showLoading() {
        SwingUtilities.invokeLater(() -> {
            if (loadingPanel != null) {
                loadingPanel.setVisible(true);
                loadingDotsTimer.start();
                // Disable the editor and results table while loading
                editor.getComponent().setEnabled(false);
                resultsTable.setEnabled(false);
            }
        });
    }

    public void hideLoading() {
        SwingUtilities.invokeLater(() -> {
            if (loadingPanel != null) {
                loadingPanel.setVisible(false);
                loadingDotsTimer.stop();
                loadingLabel.setText("Loading");
                loadingDots = 0;
                // Re-enable the editor and results table
                editor.getComponent().setEnabled(true);
                resultsTable.setEnabled(true);
            }
        });
    }

    public Editor getEditor() {
        return editor;
    }

    public void setQueryResults(java.util.List<String> columnNames, java.util.List<java.util.List<String>> rows) {
        SwingUtilities.invokeLater(() -> {
            try {
                tableModel.setRowCount(0);
                tableModel.setColumnCount(0);

                // Set column names
                for (String columnName : columnNames) {
                    tableModel.addColumn(columnName);
                }

                // Add rows
                for (java.util.List<String> row : rows) {
                    Object[] formattedRow = row.stream()
                        .map(this::formatValue)
                        .toArray();
                    tableModel.addRow(formattedRow);
                }

                // Adjust column widths
                for (int i = 0; i < resultsTable.getColumnCount(); i++) {
                    int maxWidth = 0;
                    // Check column header width
                    String header = resultsTable.getColumnName(i);
                    maxWidth = header != null ? header.length() * 10 : 0;
                    
                    // Check data widths
                    for (int j = 0; j < resultsTable.getRowCount(); j++) {
                        Object value = resultsTable.getValueAt(j, i);
                        int width = value != null ? value.toString().length() * 10 : 0;
                        maxWidth = Math.max(maxWidth, width);
                    }
                    resultsTable.getColumnModel().getColumn(i).setPreferredWidth(maxWidth + 20);
                }

                // Make sure the results are visible
                resultsTable.revalidate();
                resultsTable.repaint();
                resultsPanel.revalidate();
                resultsPanel.repaint();
                splitPane.revalidate();
                splitPane.repaint();

                // Log the results for debugging
                System.out.println("Query results updated: " + columnNames.size() + " columns, " + rows.size() + " rows");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private String formatValue(String value) {
        if (value == null) {
            return "null";
        }

        // Check if it's a UDT value
        if (value.contains("DefaultUdtValue")) {
            return formatUdtValue(value);
        }

        return value;
    }

    private String formatUdtValue(String udtValue) {
        try {
            // Remove the DefaultUdtValue wrapper
            String content = udtValue.substring(udtValue.indexOf("{") + 1, udtValue.lastIndexOf("}"));
            
            // Split the fields
            String[] fields = content.split(",");
            StringBuilder formatted = new StringBuilder("{");
            
            for (int i = 0; i < fields.length; i++) {
                String field = fields[i].trim();
                // Skip empty fields
                if (field.isEmpty()) continue;
                
                // Format each field
                String[] parts = field.split(":");
                if (parts.length == 2) {
                    String fieldName = parts[0].trim();
                    String fieldValue = parts[1].trim();
                    
                    // Add the formatted field
                    if (i > 0) formatted.append(", ");
                    formatted.append(fieldName).append(": ").append(fieldValue);
                }
            }
            
            formatted.append("}");
            return formatted.toString();
            
        } catch (Exception e) {
            // If any error occurs during formatting, return the original value
            System.err.println("Error formatting UDT value: " + e.getMessage());
            return udtValue;
        }
    }

    public void clearResults() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            resultsTable.revalidate();
            resultsTable.repaint();
        });
    }

    public void dispose() {
        EditorFactory.getInstance().releaseEditor(editor);
        loadingDotsTimer.stop();
    }
} 