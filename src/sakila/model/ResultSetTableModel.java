package sakila.model;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetTableModel extends AbstractTableModel {
    ResultSet resultSet;
    int rowCount, colCount;
    String[] columnNames;

    public ResultSetTableModel(ResultSet set) throws SQLException {
        rowCount = 0;
        colCount = 0;
        columnNames = new String[0];
        setResultSet(set);
    }

    public ResultSetTableModel() {
        rowCount = 0;
        colCount = 0;
        columnNames = new String[0];
        resultSet = null;
    }

    public void setResultSet(ResultSet set) throws SQLException {
        this.resultSet = set;
        if(set == null) {
            rowCount = 0;
            colCount = 0;
            columnNames = new String[0];
            return;
        }

        ResultSetMetaData meta = set.getMetaData();
        set.last();

        this.rowCount = set.getRow();
        this.colCount = meta.getColumnCount();

        boolean structureChanged = colCount != columnNames.length;
        String[] cols = new String[colCount];
        for(int i = 0; i < colCount; i++) {
            cols[i] = set.getMetaData().getColumnName(i+1);
            if(structureChanged) continue;
            if(!cols[i].equals(columnNames[i])) structureChanged = true;
        }
        this.columnNames = cols;
        if(structureChanged) fireTableStructureChanged();

        fireTableDataChanged();
    }
    public void setColumns(String[] columns) throws IllegalArgumentException {
        if(columns.length != columnNames.length)
            throw new IllegalArgumentException("Invalid number of columns (excepted " + columnNames.length + ")");

        columnNames = columns.clone();
        colCount = columns.length;
        fireTableStructureChanged();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return colCount;
    }

    @Override
    public Object getValueAt(int row, int col) {
        try {
            resultSet.first();
            for(int j = 0; j < row; j++) {
                resultSet.next();
            }
            return resultSet.getObject(col+1);
        }
        catch(SQLException e) {
            return null;
        }
    }
}
