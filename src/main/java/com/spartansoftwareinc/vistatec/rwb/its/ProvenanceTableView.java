package com.spartansoftwareinc.vistatec.rwb.its;

import com.spartansoftwareinc.vistatec.rwb.segment.Segment;
import com.spartansoftwareinc.vistatec.rwb.segment.SegmentAttributeView;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Table View for displaying segment ITS Provenance metadata.
 */
public class ProvenanceTableView extends JScrollPane {
    private SegmentAttributeView segAttrView;
    protected JTable provTable;
    private ProvTableModel provTableModel;
    private ListSelectionModel tableSelectionModel;
    private TableRowSorter sort;

    public ProvenanceTableView(SegmentAttributeView sav) {
        segAttrView = sav;
    }

    public void setSegment(Segment seg) {
        setViewportView(null);
        provTableModel = new ProvTableModel();
        provTable = new JTable(provTableModel);

        tableSelectionModel = provTable.getSelectionModel();
        tableSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableSelectionModel.addListSelectionListener(new ProvSelectionHandler());

        List<Provenance> provData = seg.getProv();
        provTableModel.setRows(provData);

        sort = new TableRowSorter(provTableModel);
        provTable.setRowSorter(sort);

        setViewportView(provTable);
    }
    
    public void clearSegment() {
        if (provTable != null) {
            provTable.clearSelection();
            provTableModel.deleteRows();
            provTable.setRowSorter(null);
        }
        setViewportView(null);
    }

    public void selectedProv() {
        int rowIndex = provTable.getSelectedRow();
        if (rowIndex >= 0) {
            segAttrView.setSelectedMetadata(provTableModel.rows.get(rowIndex));
        }
    }

    public void deselectProv() {
        if (provTable != null) {
            provTable.clearSelection();
        }
    }

    public class ProvTableModel extends AbstractTableModel {
        public static final int NUMCOLS = 6;
        public String [] colNames = {"Person", "Org", "Tool",
            "RevPerson", "RevOrg", "RevTool", "ProvRef"};
        private List<Provenance> rows;

        public void setRows(List<Provenance> attrs) {
            rows = attrs;
        }

        public void deleteRows() {
            rows.clear();
        }

        @Override
        public int getRowCount() {
            return rows.size();
        }

        @Override
        public int getColumnCount() {
            return NUMCOLS;
        }

        @Override
        public String getColumnName(int col) {
            return col < NUMCOLS ? colNames[col] : "";
        }

        @Override
        public Object getValueAt(int row, int col) {
            Object tableCell;
            switch (col) {
                case 0:
                    tableCell = rows.get(row).getPerson();
                    break;

                case 1:
                    tableCell = rows.get(row).getOrg();
                    break;

                case 2:
                    tableCell = rows.get(row).getTool();
                    break;

                case 3:
                    tableCell = rows.get(row).getRevPerson();
                    break;

                case 4:
                    tableCell = rows.get(row).getRevOrg();
                    break;

                case 5:
                    tableCell = rows.get(row).getRevTool();
                    break;

                case 6:
                    tableCell = rows.get(row).getProvRef();
                    break;

                default:
                    throw new IllegalArgumentException("Incorrect number of columns: "+col);
            }
            return tableCell;
        }
    }

    public class ProvSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            selectedProv();
        }
    }
}