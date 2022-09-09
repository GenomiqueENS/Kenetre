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

import java.io.IOException;

/**
 * This interface define a translator output format.
 * @since 2.0
 * @author Laurent Jourdren
 */
public interface TranslatorOutputFormat {

  /**
   * Add an header field.
   * @param fieldName name of the field
   * @throws IOException if an error occurs
   */
  void addHeaderField(String fieldName) throws IOException;

  /**
   * Add a mew line.
   * @throws IOException if an error occurs
   */
  void newLine() throws IOException;

  /**
   * Add an empty value.
   * @throws IOException if an error occurs
   */
  void writeEmpty() throws IOException;

  /**
   * Add a long value.
   * @param l long value
   * @throws IOException if an error occurs
   */
  void writeLong(long l) throws IOException;

  /**
   * Add a double value.
   * @param d double value
   * @throws IOException if an error occurs
   */
  void writeDouble(double d) throws IOException;

  /**
   * Add a text value.
   * @param text text value
   * @throws IOException if an error occurs
   */
  void writeText(String text) throws IOException;

  /**
   * Add a link value.
   * @param text text value
   * @param link link value
   * @throws IOException if an error occurs
   */
  void writeLink(String text, String link) throws IOException;

  /**
   * Close the output format.
   * @throws IOException if an error occurs
   */
  void close() throws IOException;
}
