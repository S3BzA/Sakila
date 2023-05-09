package sakila.model;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class ResultSetTableModel extends AbstractTableModel {
    ResultSet resultSet;
    int rowCount, colCount;
    String[] columnNames;
    String[] columnTypes;
    boolean isModifiable = false;

    public ResultSetTableModel(ResultSet set) throws SQLException {
        setResultSet(set);
    }

    public ResultSetTableModel() {
        try {
            setResultSet(null);
        }
        catch (SQLException ignored) {}
    }

    public void setResultSet(ResultSet set) throws SQLException {
        this.resultSet = set;
        if(set == null) {
            rowCount = 0;
            colCount = 0;
            columnNames = new String[0];
            columnTypes = new String[0];
            return;
        }

        isModifiable = set.getConcurrency() == ResultSet.CONCUR_UPDATABLE;
        ResultSetMetaData meta = set.getMetaData();
        set.last();

        this.rowCount = set.getRow();
        this.colCount = meta.getColumnCount();

        boolean structureChanged = colCount != columnNames.length;
        String[] cols = new String[colCount];
        String[] colTypes = new String[colCount];
        for(int i = 0; i < colCount; i++) {
            cols[i] = meta.getColumnName(i+1);
            colTypes[i] = meta.getColumnClassName(i+1);
            if(structureChanged) continue;
            if(!cols[i].equals(columnNames[i])) structureChanged = true;
            if(!colTypes[i].equals(columnTypes[i])) structureChanged = true;
        }
        this.columnNames = cols;
        this.columnTypes = colTypes;
        if(structureChanged) fireTableStructureChanged();
        else fireTableDataChanged();
    }
    public void setColumns(String[] columns) throws IllegalArgumentException {
        if(columns.length != columnNames.length)
            throw new IllegalArgumentException("Invalid number of columns (excepted " + columnNames.length + ")");

        columnNames = columns.clone();
        colCount = columns.length;
        fireTableStructureChanged();
    }

    // Source: https://www.tutorialspoint.com/java-resultsetmetadata-getcolumntype-method-with-example
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        try {
            return Class.forName(columnTypes[columnIndex]);
        } catch (ClassNotFoundException ignored) {
            return Object.class;
        }
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
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return isModifiable;
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

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            resultSet.absolute(rowIndex + 1);
            resultSet.updateObject(columnIndex+1, aValue);
            resultSet.updateTimestamp("last_update", Timestamp.from(Instant.now()));
            resultSet.updateRow();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
