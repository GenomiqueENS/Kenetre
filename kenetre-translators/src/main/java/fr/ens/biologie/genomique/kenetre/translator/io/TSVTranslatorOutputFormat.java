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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * This class define a TranslatorOutputFormat that generate a tabular text file.
 * @since 2.0
 * @author Laurent Jourdren
 */
public class TSVTranslatorOutputFormat implements TranslatorOutputFormat {

  private final BufferedWriter writer;

  private boolean startOfLine = true;

  @Override
  public void addHeaderField(final String fieldName) throws IOException {

    if (this.startOfLine) {
      this.startOfLine = false;
    } else {
      this.writer.write('\t');
    }

    this.writer.write(fieldName);
  }

  @Override
  public void newLine() throws IOException {

    this.writer.write('\n');
    this.startOfLine = true;
  }

  @Override
  public void writeEmpty() throws IOException {

    if (this.startOfLine) {
      this.startOfLine = false;
    } else {
      this.writer.write('\t');
    }

  }

  @Override
  public void writeLong(final long l) throws IOException {

    if (this.startOfLine) {
      this.startOfLine = false;
    } else {
      this.writer.write('\t');
    }

    this.writer.write(Long.toString(l));

  }

  @Override
  public void writeDouble(final double d) throws IOException {

    if (this.startOfLine) {
      this.startOfLine = false;
    } else {
      this.writer.write('\t');
    }

    this.writer.write(Double.toString(d));

  }

  @Override
  public void writeText(final String text) throws IOException {

    if (this.startOfLine) {
      this.startOfLine = false;
    } else {
      this.writer.write('\t');
    }

    if (text != null) {
      this.writer.write(text);
    }

  }

  @Override
  public void writeLink(final String text, final String link)
      throws IOException {

    writeText(text);
  }

  @Override
  public void close() throws IOException {

    this.writer.close();
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param os output stream
   */
  public TSVTranslatorOutputFormat(final OutputStream os) {

    this.writer =
        new BufferedWriter(new OutputStreamWriter(os, Charset.defaultCharset()));
  }

  /**
   * Public constructor.
   * @param file output file
   */
  public TSVTranslatorOutputFormat(final File file) throws IOException {

    this.writer =
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),
        		Charset.defaultCharset()));
  }

}
