package fr.ens.biologie.genomique.kenetre.bio.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Objects;

import fr.ens.biologie.genomique.kenetre.bio.ExpressionMatrix;

/**
 * This class define an ExpressionMatrix writer for TSV format.
 * @author Laurent Jourdren
 * @since 2.0
 */
public class TSVExpressionMatrixWriter implements ExpressionMatrixWriter {

  private final OutputStream os;

  @Override
  public void write(ExpressionMatrix matrix) throws IOException {

    Objects.requireNonNull(matrix, "matrix argument cannot be null");

    write(matrix, matrix.getRowNames());
  }

  @Override
  public void write(final ExpressionMatrix matrix,
      final Collection<String> rowNamesToWrite) throws IOException {

    Objects.requireNonNull(matrix, "matrix argument cannot be null");
    Objects.requireNonNull(rowNamesToWrite,
        "rowNamesToWrite argument cannot be null");

    try (Writer writer = new OutputStreamWriter(this.os)) {

      StringBuilder sb = new StringBuilder();

      for (String columnName : matrix.getColumnNames()) {
        sb.append('\t');
        sb.append(columnName);
      }
      sb.append('\n');
      writer.write(sb.toString());

      for (String rowName : rowNamesToWrite) {
        sb.setLength(0);

        sb.append(rowName);

        for (Double value : matrix.getRowValues(rowName)) {
          sb.append('\t');

          double d = value;
          if (!(Double.isNaN(d) || Double.isInfinite(d))
              && Math.floor(d) - d == 0.0) {
            sb.append((int) d);
          } else {
            sb.append(value);
          }
        }
        sb.append('\n');
        writer.write((sb.toString()));
      }
    }
  }

  @Override
  public void close() throws IOException {

    this.os.close();
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param os OutputStream to use
   */
  public TSVExpressionMatrixWriter(final OutputStream os)
      throws FileNotFoundException {

    Objects.requireNonNull(os, "os argument cannot be null");

    this.os = os;
  }

  /**
   * Public constructor.
   * @param outputFile file to use
   */
  public TSVExpressionMatrixWriter(final File outputFile) throws IOException {

    Objects.requireNonNull(outputFile, "os argument cannot be null");

    this.os = new FileOutputStream(outputFile);
  }

  /**
   * Public constructor.
   * @param outputFilename name of the file to use
   */
  public TSVExpressionMatrixWriter(final String outputFilename)
      throws IOException {

    Objects.requireNonNull(outputFilename, "os argument cannot be null");

    this.os = new FileOutputStream(outputFilename);
  }

}
