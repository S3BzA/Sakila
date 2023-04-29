package sakila.model;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetTableModel extends AbstractTableModel {
    ResultSet resultSet;
    int rowCount, colCount;
    String[] columnNames;

    public ResultSetTableModel(ResultSet set) throws SQLException {
        this.resultSet = set;

        set.last();
        this.rowCount = set.getRow();
        this.colCount = set.getMetaData().getColumnCount();

        columnNames = new String[colCount];
        for(int i = 0; i < colCount; i++) {
            columnNames[i] = set.getMetaData().getColumnName(i+1);
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
