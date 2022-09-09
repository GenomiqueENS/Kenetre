package fr.ens.biologie.genomique.kenetre.bio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * This class define a dense matrix. This a generalization of the
 * DenseExpressionMatrix.
 * @author Laurent Jourdren
 * @since 2.4
 */
public class DenseMatrix<E> extends AbstractMatrix<E> {

  private final Multimap<String, E> values = ArrayListMultimap.create();
  private final Map<String, Integer> columnIndex = new HashMap<>();
  private final Set<String> rowOrder = new LinkedHashSet<>();
  private final E defaultValue;

  //
  // Getters
  //

  @Override
  public List<String> getRowNames() {

    return Collections.unmodifiableList(new ArrayList<>(this.rowOrder));
  }

  @Override
  public int getRowCount() {

    return this.rowOrder.size();
  }

  @Override
  public List<String> getColumnNames() {

    String[] result = new String[this.columnIndex.size()];

    for (Map.Entry<String, Integer> e : this.columnIndex.entrySet()) {
      result[e.getValue()] = e.getKey();
    }

    return Collections.unmodifiableList(Arrays.asList(result));
  }

  @Override
  public int getColumnCount() {

    return this.columnIndex.size();
  }

  @Override
  public List<E> getColumnValues(final String columnName) {

    Objects.requireNonNull(columnName, "columnName argument cannot be null");

    List<E> result = new ArrayList<>(this.rowOrder.size());

    for (String rowName : this.rowOrder) {
      result.add(getValue(rowName, columnName));
    }

    return Collections.unmodifiableList(result);
  }

  @Override
  public List<E> getRowValues(final String rowName) {

    Objects.requireNonNull(rowName, "rowName argument cannot be null");

    return (List<E>) this.values.get(rowName);
  }

  @Override
  public E getValue(final String rowName, final String columnName) {

    Objects.requireNonNull(rowName, "rowName argument cannot be null");
    Objects.requireNonNull(columnName, "columnName argument cannot be null");

    if (!this.columnIndex.containsKey(columnName)) {
      throw new IllegalArgumentException("Unknown column name: " + columnName);
    }

    if (!this.rowOrder.contains(rowName)) {
      throw new IllegalArgumentException("Unknown row name: " + rowName);
    }

    return getRowValues(rowName).get(this.columnIndex.get(columnName));
  }

  @Override
  public boolean containsColumn(final String columnName) {

    return this.columnIndex.containsKey(columnName);
  }

  @Override
  public boolean containsRow(final String rowName) {

    if (this.columnIndex.size() == 0) {
      return this.rowOrder.contains(rowName);
    }

    return this.values.containsKey(rowName);
  }

  //
  // Setters
  //

  @Override
  public void setValue(final String rowName, final String columnName,
      final E value) {

    Objects.requireNonNull(rowName, "rowName argument cannot be null");
    Objects.requireNonNull(columnName, "columnName argument cannot be null");

    // Check if a column must be added
    if (!this.columnIndex.containsKey(columnName)) {
      addColumn(columnName);
    }

    // Check if a row must be added
    if (!this.values.containsKey(rowName)) {
      addRow(rowName);
    }

    // Set the value
    ((List<E>) this.values.get(rowName)).set(this.columnIndex.get(columnName),
        value);
  }

  @Override
  public void addRow(final String rowName) {

    Objects.requireNonNull(rowName, "rowName argument cannot be null");

    if (this.rowOrder.contains(rowName)) {
      return;
    }

    // Add the default values
    this.values.putAll(rowName,
        Collections.nCopies(this.columnIndex.size(), this.defaultValue));

    // Add the row name in the order of row
    this.rowOrder.add(rowName);
  }

  @Override
  public void addColumn(final String columnName) {

    Objects.requireNonNull(columnName, "columnName argument cannot be null");

    if (this.columnIndex.containsKey(columnName)) {
      return;
    }

    // Add the column name in the index of column
    this.columnIndex.put(columnName, this.columnIndex.size());

    if (this.columnIndex.size() == 1 && !this.rowOrder.isEmpty()) {

      // Fill existing empty rows
      for (String rowName : this.rowOrder) {
        this.values.put(rowName, this.defaultValue);
      }
    } else {

      // Fill the new column
      for (Collection<E> list : this.values.asMap().values()) {
        list.add(this.defaultValue);
      }
    }

  }

  @Override
  public void renameColumn(final String oldColumnName,
      final String newColumnName) {

    Objects.requireNonNull(oldColumnName, "oldColumnName cannot be null");
    Objects.requireNonNull(newColumnName, "newColumnName cannot be null");

    if (!containsColumn(oldColumnName)) {
      throw new IllegalArgumentException(
          "Unknown column name: " + oldColumnName);
    }

    if (containsColumn(newColumnName)) {
      throw new IllegalArgumentException(
          "The new column name already exists: " + newColumnName);
    }

    this.columnIndex.put(newColumnName, this.columnIndex.get(oldColumnName));
    this.columnIndex.remove(oldColumnName);
  }

  @Override
  public void removeColumn(final String columnName) {

    Objects.requireNonNull(columnName, "columnName argument cannot be null");

    if (!this.columnIndex.containsKey(columnName)) {
      throw new IllegalArgumentException(
          "columnName does not exists: " + columnName);
    }

    // Get columnIndex
    int columnIndex = this.columnIndex.get(columnName);

    // Remove the column values
    for (Collection<E> list : this.values.asMap().values()) {
      ((List<E>) list).remove(columnIndex);
    }

    // Remove the column
    this.columnIndex.remove(columnName);
  }

  @Override
  public void removeRow(final String rowName) {

    Objects.requireNonNull(rowName, "rowName argument cannot be null");

    if (!this.rowOrder.contains(rowName)) {
      throw new IllegalArgumentException("rowName does not exists: " + rowName);
    }

    // Add the default values
    this.values.removeAll(rowName);

    // Add the row name in the order of row
    this.rowOrder.remove(rowName);
  }

  //
  // Default value
  //

  @Override
  public E getDefaultValue() {

    return this.defaultValue;
  }

  //
  // Object methods
  //

  @Override
  public boolean equals(Object o) {

    if (o == this) {
      return true;
    }

    if (!(o instanceof DenseExpressionMatrix)) {
      return false;
    }

    @SuppressWarnings("unchecked")
    final DenseMatrix<E> that = (DenseMatrix<E>) o;

    return Objects.equals(this.values, that.values)
        && Objects.equals(this.columnIndex, that.columnIndex)
        && Objects.equals(this.rowOrder, that.rowOrder);
  }

  @Override
  public int hashCode() {

    return Objects.hash(this.values, this.columnIndex, this.rowOrder);
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param defaultValue the default value of the matrix
   */
  public DenseMatrix(final E defaultValue) {
    this.defaultValue = defaultValue;
  }

}
