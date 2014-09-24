package com.vistatec.ocelot;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import com.vistatec.ocelot.rules.DataCategoryFlag;
import com.vistatec.ocelot.rules.DataCategoryFlagRenderer;
import com.vistatec.ocelot.rules.RuleConfiguration;
import com.vistatec.ocelot.segment.Segment;
import com.vistatec.ocelot.segment.SegmentModel;
import com.vistatec.ocelot.segment.SegmentTableModel;
import com.vistatec.ocelot.ui.OTable;

public class ColumnSelector extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    private SegmentTableModel model;
    private ColumnTable table;
    protected EnumMap<SegmentViewColumn, Boolean> enabledColumns =
            new EnumMap<SegmentViewColumn, Boolean>(SegmentViewColumn.class);
    private Window parent;
    
    public ColumnSelector(Window window, SegmentTableModel tableModel) {
        super(new GridBagLayout());
        this.parent = window;
        this.model = tableModel;
        enabledColumns.putAll(model.getColumnEnabledStates());

        setBorder(new EmptyBorder(10,10,10,10));

        GridBagConstraints gridBag = new GridBagConstraints();
        gridBag.anchor = GridBagConstraints.FIRST_LINE_START;
        gridBag.gridwidth = 2;
        int gridy = 0;

        JLabel title = new JLabel("Select columns to display:");
        gridBag.gridx = 0;
        gridBag.gridy = gridy++;
        gridBag.insets = new Insets(10, 0, 10, 0);
        gridBag.fill = GridBagConstraints.HORIZONTAL;
        add(title, gridBag);

        this.table = new ColumnTable();
        gridBag = new GridBagConstraints();
        gridBag.gridx = 0;
        gridBag.gridy = gridy++;
        gridBag.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.fill = GridBagConstraints.HORIZONTAL;
        gridBag.insets = new Insets(5, 5, 5, 5);
        add(table.getTable(), gridBag);

        JButton cancel = new JButton("Cancel");
        gridBag = new GridBagConstraints();
        gridBag.gridx = 0;
        gridBag.gridy = gridy++;
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.dispose();
            }
        });
        add(cancel, gridBag);

        JButton ok = new JButton("OK");
        gridBag.gridx = 1;
        ok.addActionListener(this);
        add(ok, gridBag);

    }
    
    // when I close this, I need to call tableModel.fireTableStructureChanged() 
    @Override
    public void actionPerformed(ActionEvent event) {
        // Sync table data back to the model
        for (Map.Entry<SegmentViewColumn, Boolean> e : enabledColumns.entrySet()) {
            model.setColumnEnabled(e.getKey(), e.getValue());
        }
        parent.dispose();
        model.fireTableStructureChanged();
    }

    public class ColumnTable {
        private JTable table;

        public ColumnTable() {
            this.table = createTable();
        }

        public JTable getTable() {
            return table;
        }

        
        class TableModel extends AbstractTableModel {
            private static final long serialVersionUID = 1L;

            @Override
            public int getRowCount() {
                return SegmentViewColumn.values().length;
            }

            @Override
            public Object getValueAt(int row, int column) {
                SegmentViewColumn col = getColumnForRow(row);
                switch (column) {
                case 0:
                    return enabledColumns.get(col);
                case 1:
                    return col.getFullName();
                }
                throw new IllegalArgumentException("Invalid column " + column);
            }

            @Override
            public void setValueAt(Object obj, int row, int column) {
                if (obj instanceof Boolean && column == 0) {
                    SegmentViewColumn col= getColumnForRow(row);
                    enabledColumns.put(col, (Boolean)obj);
                    fireTableCellUpdated(row, column);
                }
            }

            private SegmentViewColumn getColumnForRow(int row) {
                return Arrays.asList(SegmentViewColumn.values()).get(row);
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                case 0:
                    return Boolean.class;
                case 1:
                    return String.class;
                }
                throw new IllegalArgumentException("Invalid column " + columnIndex);
            }
        }

        protected JTable createTable() {
            TableModel tableModel = new TableModel();
            JTable table = new OTable(tableModel);
            table.setTableHeader(null);
            table.setCellSelectionEnabled(false);
            table.setShowGrid(false);
            table.setDefaultRenderer(DataCategoryFlag.class, new DataCategoryFlagRenderer());
            // Add a little little breathing room, particularly around the edge
            table.setRowHeight(table.getRowHeight() + 4);

            TableColumnModel columnModel = table.getColumnModel();
            // Hack - size the column to fit a checkbox exactly.  Otherwise
            // there's a rendering glitch where the checkbox can jump around
            // in the cell when clicked.
            JCheckBox cb = new JCheckBox();
            int cbWidth = cb.getPreferredSize().width + 4;
            columnModel.getColumn(0).setMinWidth(cbWidth);
            columnModel.getColumn(0).setPreferredWidth(cbWidth);
            columnModel.getColumn(0).setMaxWidth(cbWidth);
            columnModel.getColumn(1).setMinWidth(100);
            columnModel.getColumn(1).setPreferredWidth(150);
            columnModel.getColumn(1).setMaxWidth(150);
            
            return table;
        }
    }

    
    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame();
            ColumnSelector selector = new ColumnSelector(frame, createModel());
            frame.add(selector);
            frame.pack();
            frame.setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SegmentTableModel createModel() throws Exception {
        return new SegmentTableModel(new SegmentModel() {
            @Override
            public Segment getSegment(int row) {
                return null;
            }
            
            @Override
            public int getNumSegments() {
                return 0;
            }
        }, new RuleConfiguration());
    }
}
