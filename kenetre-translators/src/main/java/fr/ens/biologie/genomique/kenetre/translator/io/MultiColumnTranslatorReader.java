/*
 *                  Eoulsan development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public License version 2.1 or
 * later and CeCILL-C. This should be distributed with the code.
 * If you do not have a copy, see:
 *
 *      http://www.gnu.org/licenses/lgpl-2.1.txt
 *      http://www.cecill.info/licences/Licence_CeCILL-C_V1-en.txt
 *
 * Copyright for this code is held jointly by the Genomic platform
 * of the Institut de Biologie de l'École normale supérieure and
 * the individual authors. These should be listed in @author doc
 * comments.
 *
 * For more information on the Eoulsan project and its aims,
 * or to join the Eoulsan Google group, visit the home page
 * at:
 *
 *      http://outils.genomique.biologie.ens.fr/eoulsan
 *
 */

package fr.ens.biologie.genomique.kenetre.translator.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.ens.biologie.genomique.kenetre.translator.MultiColumnTranslator;

/**
 * This class define a reader for load annotation into a translator.
 * @since 2.0
 * @author Laurent Jourdren T
 */
public class MultiColumnTranslatorReader {

  // TODO put removeDoubleQuotesAndTrim to StringUtils

  private InputStream is;

  private BufferedReader bufferedReader;
  private static final String SEPARATOR = "\t";
  private boolean removeQuotes = true;
  private final boolean noHeader;

  //
  // Getters
  //

  /**
   * Get the input stream.
   * @return Returns the input stream
   */
  protected InputStream getInputStream() {
    return this.is;
  }

  /**
   * Get the buffered reader of the stream.
   * @return Returns the bufferedReader
   */
  protected BufferedReader getBufferedReader() {
    return this.bufferedReader;
  }

  /**
   * Get the separator field of the file.
   * @return The separator field of the file
   */
  protected String getSeparatorField() {
    return SEPARATOR;
  }

  /**
   * Test if quotes of the fields must be removed
   * @return Returns the removeQuotes
   */
  public boolean isRemoveQuotes() {
    return this.removeQuotes;
  }

  //
  // Setters
  //

  /**
   * Set the buffered reader of the stream.
   * @param bufferedReader The bufferedReader to set
   */
  protected void setBufferedReader(final BufferedReader bufferedReader) {
    this.bufferedReader = bufferedReader;
  }

  /**
   * Set the input stream.
   * @param is The input stream to set
   * @throws IOException if the stream is null
   */
  protected void setInputStream(final InputStream is) throws IOException {

    if (is == null) {
      throw new IOException("No stream to read");
    }
    this.is = is;
  }

  /**
   * Set if the quotes of the fields must be removed
   * @param removeQuotes The removeQuotes to set
   */
  public void setRemoveQuotes(final boolean removeQuotes) {
    this.removeQuotes = removeQuotes;
  }

  //
  // Other methods
  //

  /**
   * Read the design.
   * @return a new Design object
   * @throws IOException if an error occurs while reading the design
   */
  public MultiColumnTranslator read() throws IOException {

    setBufferedReader(new BufferedReader(
        new InputStreamReader(getInputStream(), Charset.defaultCharset())));

    final boolean removeQuotes = isRemoveQuotes();

    BufferedReader br = getBufferedReader();
    final String separator = getSeparatorField();
    String line = null;

    MultiColumnTranslator result = null;

    while ((line = br.readLine()) != null) {

      if ("".equals(line)) {
        continue;
      }

      // String[] cols = line.split(separator);
      // List<String> cols = new ArrayList<>(line.split(separator).length);
      List<String> cols = new ArrayList<>(Arrays.asList(line.split(separator)));
      if (removeQuotes) {
        for (int i = 0; i < cols.size(); i++) {
          cols.set(i, removeDoubleQuotesAndTrim(cols.get(i)));
        }
      }

      if (result == null && this.noHeader) {
        // final String[] header = new String[cols.length];
        final List<String> header = new ArrayList<>(cols.size());
        for (int i = 0; i < header.size(); i++) {
          header.add("#" + i);
        }
        result = new MultiColumnTranslator(header);
      }

      if (result == null) {
        result = new MultiColumnTranslator(cols);
      } else {
        result.addRow(cols);
      }

    }

    return result;
  }

  //
  // Utility methods
  //

  /**
   * Remove double quote from a string.
   * @param s The string parameter
   * @return a string without double quotes
   */
  private static String removeDoubleQuotes(final String s) {

    if (s == null) {
      return null;
    }

    String result = s;

    if (result.startsWith("\"")) {
      result = result.substring(1);
    }
    if (result.endsWith("\"")) {
      result = result.substring(0, result.length() - 1);
    }

    return result;
  }

  /**
   * Remove double quote and trim a string.
   * @param s The string parameter
   * @return a string without space and double quotes
   */
  private static String removeDoubleQuotesAndTrim(final String s) {

    if (s == null) {
      return null;
    }

    return removeDoubleQuotes(s.trim());
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param filename file to read
   * @throws IOException if an error occurs while reading the file or if the
   *           file is null.
   */
  public MultiColumnTranslatorReader(final String filename) throws IOException {

    this(new File(filename), false);
  }

  /**
   * Public constructor.
   * @param filename file to read
   * @param noHeader true if there is no header for column names
   * @throws IOException if an error occurs while reading the file or if the
   *           file is null.
   */
  public MultiColumnTranslatorReader(final String filename,
      final boolean noHeader) throws IOException {

    this(new File(filename), noHeader);
  }

  /**
   * Public constructor.
   * @param file file to read
   * @throws IOException if an error occurs while reading the file or if the
   *           file is null.
   */
  public MultiColumnTranslatorReader(final File file) throws IOException {

    this(file, false);
  }

  /**
   * Public constructor.
   * @param file file to read
   * @param noHeader true if there is no header for column names
   * @throws IOException if an error occurs while reading the file or if the
   *           file is null.
   */
  public MultiColumnTranslatorReader(final File file, final boolean noHeader)
      throws IOException {

    if (file == null) {
      throw new IOException("No file to load");
    }

    this.noHeader = noHeader;

    setInputStream(new FileInputStream(file));
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws IOException if the stream is null
   */
  public MultiColumnTranslatorReader(final InputStream is) throws IOException {

    this(is, false);
  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @param noHeader true if there is no header for column names
   * @throws IOException if the stream is null
   */
  public MultiColumnTranslatorReader(final InputStream is,
      final boolean noHeader) throws IOException {

    this.noHeader = noHeader;
    setInputStream(is);
  }

}
