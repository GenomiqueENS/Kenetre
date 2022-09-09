package fr.ens.biologie.genomique.kenetre.bio;

import java.util.Collection;
import java.util.List;

/**
 * This interface define a matrix. This a generalization of the
 * ExpressionMatrix.
 * @author Laurent Jourdren
 * @since 2.4
 */
public interface Matrix<E> {

  interface Entry<E> {

    /**
     * Get the name of the row of the entry.
     * @return the name of the row of the entry
     */
    String getRowName();

    /**
     * Get the name of the column of the entry.
     * @return the name of the column of the entry
     */
    String getColumnName();

    /**
     * Get the value of the entry
     * @return the value of the entry
     */
    E getValue();
  }

  /**
   * Get the row names.
   * @return the row names
   */
  List<String> getRowNames();

  /**
   * Get the row count.
   * @return the row count
   */
  int getRowCount();

  /**
   * Get the column names.
   * @return the row names
   */
  List<String> getColumnNames();

  /**
   * Get the column count.
   * @return the row names
   */
  int getColumnCount();

  /**
   * Get the values of the matrix.
   * @return the values of the matrix
   */
  Iterable<Entry<E>> values();

  /**
   * Get the non zero values of the matrix.
   * @return the non zero values of the matrix
   */
  Iterable<Entry<E>> nonZeroValues();

  /**
   * Get the values of a column.
   * @param columnName the column name
   * @return a list with the values of the column
   */
  List<E> getColumnValues(String columnName);

  /**
   * Get the row values.
   * @param rowName the row name
   * @return a list with the row values
   */
  List<E> getRowValues(String rowName);

  /**
   * Get a value of the matrix
   * @param rowName the row name
   * @param columnName the column name
   * @return the value of the cell
   */
  E getValue(String rowName, String columnName);

  /**
   * Test if a column exists
   * @param columnName the name of the column
   * @return true if the column exists
   */
  boolean containsColumn(String columnName);

  /**
   * Test if a row exists
   * @param rowName the name of the row
   * @return true if the row exists
   */
  boolean containsRow(String rowName);

  /**
   * Set a value of the matrix.
   * @param rowName row name
   * @param columnName column name
   * @param value the value to set
   */
  void setValue(String rowName, String columnName, E value);

  /**
   * Add new rows.
   * @param rowNames the names of the row to add
   */
  void addRows(List<String> rowNames);

  /**
   * Add new rows.
   * @param rowNames the names of the row to add
   */
  void addRows(String... rowNames);

  /**
   * Add a new row.
   * @param rowName the name of the row to add
   */
  void addRow(String rowName);

  /**
   * Add a new columns.
   * @param columnNames the names of the new columns
   */
  void addColumns(List<String> columnNames);

  /**
   * Add a new columns.
   * @param columnNames the name of the new columns
   */
  void addColumns(String... columnNames);

  /**
   * Add a new column.
   * @param columnName the name of the new column
   */
  void addColumn(String columnName);

  /**
   * Add a matrix value to the current matrix
   * @param matrix matrix to add
   */
  void add(Matrix<E> matrix);

  /**
   * Rename a column.
   * @param oldColumnName the old column name
   * @param newColumnName the new column name
   */
  void renameColumn(String oldColumnName, String newColumnName);

  /**
   * Remove a column of the matrix.
   * @param columnName the name of the column to remove
   */
  void removeColumn(String columnName);

  /**
   * Remove columns of the matrix.
   * @param columnNames the names of the columns to remove
   */
  void removeColumns(Collection<String> columnNames);

  /**
   * Remove a row of the matrix.
   * @param rowName the name of the row to remove
   */
  void removeRow(String rowName);

  /**
   * Remove rows of the matrix.
   * @param rowNames the names of the rows to remove
   */
  void removeRows(Collection<String> rowNames);

  /**
   * Retain row of the matrix.
   * @param rowNames a collection with the row of the matrix to retain
   */
  void retainRows(Collection<String> rowNames);

  /**
   * Retain row of the matrix.
   * @param columnNames a collection with the column names of the matrix to
   *          retain
   */
  void retainColumns(Collection<String> columnNames);

  /**
   * Get the default value of a cell.
   * @return the default value of a cell
   */
  E getDefaultValue();

  /**
   * Get the size of the matrix.
   * @return the size (the number of elements) of the matrix
   */
  int size();

  /**
   * Test if the matrix is empty. A matrix is empty when the size of the matrix
   * is 0, and no row or column has been created.
   * @return true if the matrix is empty
   */
  boolean isEmpty();

}
