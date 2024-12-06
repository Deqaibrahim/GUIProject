package gui;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class DbUtils {

    public static TableModel resultSetToTableModel(ResultSet resultSet) {
        try {
            // Get metadata of the ResultSet to get column information
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Create column names array
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Create a list to store rows of data
            List<Object[]> dataList = new ArrayList<>();

            // Iterate through the ResultSet and store data into the list
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
                    row[colIndex - 1] = resultSet.getObject(colIndex);
                }
                dataList.add(row);
            }

            // Return a new TableModel
            return new AbstractTableModel() {
                @Override
                public int getColumnCount() {
                    return columnCount;
                }

                @Override
                public int getRowCount() {
                    return dataList.size(); // Number of rows from ResultSet
                }

                @Override
                public String getColumnName(int columnIndex) {
                    return columnNames[columnIndex];
                }

                @Override
                public Object getValueAt(int rowIndex, int columnIndex) {
                    return dataList.get(rowIndex)[columnIndex];
                }
            };

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
