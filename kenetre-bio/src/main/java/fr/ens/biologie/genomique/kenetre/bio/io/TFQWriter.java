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

package fr.ens.biologie.genomique.kenetre.bio.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import fr.ens.biologie.genomique.kenetre.bio.ReadSequence;
import fr.ens.biologie.genomique.kenetre.io.FileUtils;

/**
 * This class implements a TFQ writer.
 * @since 1.0
 * @author Laurent Jourdren
 */
public class TFQWriter implements ReadSequenceWriter {

  private final Writer writer;

  @Override
  public void close() throws IOException {

    this.writer.close();
  }

  @Override
  public void write(final ReadSequence readSequence) throws IOException {

    if (readSequence != null) {
      this.writer.write(readSequence.toTFQ());
    }
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param writer Writer to use
   */
  public TFQWriter(final Writer writer) {

    if (writer == null) {
      throw new NullPointerException("The writer is null.");
    }

    this.writer = writer;
  }

  /**
   * Public constructor.
   * @param os OutputStream to use
   */
  public TFQWriter(final OutputStream os) {

    this.writer = FileUtils.createFastBufferedWriter(os);
  }

  /**
   * Public constructor.
   * @param outputFile file to use
   * @throws IOException if an error occurs while creating the file
   */
  public TFQWriter(final File outputFile) throws IOException {

    this.writer = FileUtils.createFastBufferedWriter(outputFile);
  }

  /**
   * Public constructor.
   * @param outputFilename name of the file to use
   * @throws IOException if an error occurs while creating the file
   */
  public TFQWriter(final String outputFilename) throws IOException {

    this.writer = FileUtils.createFastBufferedWriter(outputFilename);
  }

}
