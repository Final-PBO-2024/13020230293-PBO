package com.absensi.util;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TabelUtils {
    
    public static void setColumnWidths(JTable table, int[] columnIndices, int[] widths) {
        if (columnIndices.length != widths.length) {
            throw new IllegalArgumentException("The length of columnIndices and widths must be the same");
        }

        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnIndices.length; i++) {
            int columnIndex = columnIndices[i];
            int width = widths[i];
            TableColumn column = columnModel.getColumn(columnIndex);
            column.setPreferredWidth(width);
            column.setMaxWidth(width);
            column.setMinWidth(width);
        }
    }

    public static void setColumnAlignment(JTable table, int[] indices, int alignment) {
        TableColumnModel columnModel = table.getColumnModel();
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(alignment);
        for (int index : indices) {
            columnModel.getColumn(index).setCellRenderer(cellRenderer);
        }
    }

    public static void setHeaderAlignment(JTable table, int[] columnIndices, int[] alignments, int defaultAlignment) {
        if (columnIndices.length != alignments.length) {
            throw new IllegalArgumentException("The length of columnIndices and alignments must be the same");
        }

        JTableHeader header = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (component instanceof JLabel) {
                    JLabel label = (JLabel) component;

                    // Set alignment based on the column
                    boolean found = false;
                    for (int i = 0; i < columnIndices.length; i++) {
                        if (column == columnIndices[i]) {
                            label.setHorizontalAlignment(alignments[i]);
                            found = true;
                            break;
                        }
                    }

                    // If not found, use default alignment
                    if (!found) {
                        label.setHorizontalAlignment(defaultAlignment);
                    }

                    Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, UIManager.getColor("Table.gridColor")); // Border bawah
                    label.setBorder(border);
                }
                return component;
            }
        };

        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setHeaderRenderer(headerRenderer);
        }

        table.setShowGrid(false);
    }
}