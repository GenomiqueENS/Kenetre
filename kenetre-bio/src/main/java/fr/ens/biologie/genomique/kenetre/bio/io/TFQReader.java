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

import static fr.ens.biologie.genomique.kenetre.bio.io.BioCharsets.FASTQ_CHARSET;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

import fr.ens.biologie.genomique.kenetre.bio.BadBioEntryException;
import fr.ens.biologie.genomique.kenetre.bio.ReadSequence;
import fr.ens.biologie.genomique.kenetre.io.FileUtils;

/**
 * This class implements a TFQ reader.
 * @since 1.0
 * @author Laurent Jourdren
 */
public class TFQReader implements ReadSequenceReader {

  private final BufferedReader reader;

  private ReadSequence result = null;

  private boolean end = false;
  private boolean nextCallDone = true;
  protected IOException ioException;

  @Override
  public void close() throws IOException {

    this.reader.close();
  }

  @Override
  public Iterator<ReadSequence> iterator() {

    return this;
  }

  @Override
  public boolean hasNext() {

    if (this.end) {
      return false;
    }

    this.nextCallDone = false;

    String line = null;

    try {
      while ((line = this.reader.readLine()) != null) {

        // Trim the line
        final String trim = line.trim();

        // discard empty lines
        if ("".equals(trim)) {
          continue;
        }

        this.result = new ReadSequence();
        this.result.parse(trim);
        return true;
      }

      this.end = true;

      return false;
    } catch (IOException e) {

      this.ioException = e;
      this.end = true;
      return false;
    }
  }

  @Override
  public ReadSequence next() {

    if (this.nextCallDone) {
      throw new NoSuchElementException();
    }

    this.nextCallDone = true;

    return this.result;
  }

  @Override
  public void remove() {

    throw new UnsupportedOperationException("Unsupported operation");
  }

  @Override
  public void throwException() throws IOException, BadBioEntryException {

    if (this.ioException != null) {
      throw this.ioException;
    }
  }

  //
  // Constructors
  //

  /**
   * Public constructor
   * @param is InputStream to use
   */
  public TFQReader(final InputStream is) {

    if (is == null) {
      throw new NullPointerException("InputStream is null");
    }

    this.reader = FileUtils.createBufferedReader(is, FASTQ_CHARSET);
  }

  /**
   * Public constructor
   * @param file File to use
   * @throws FileNotFoundException if cannot find the input file
   */
  public TFQReader(final File file) throws FileNotFoundException {

    if (file == null) {
      throw new NullPointerException("File is null");
    }

    this.reader = FileUtils.createBufferedReader(file, FASTQ_CHARSET);
  }

  /**
   * Public constructor
   * @param filename File to use
   * @throws FileNotFoundException if cannot find the input file
   */
  public TFQReader(final String filename) throws FileNotFoundException {

    if (filename == null) {
      throw new NullPointerException("File is null");
    }

    this.reader = FileUtils.createBufferedReader(filename, FASTQ_CHARSET);
  }

}
