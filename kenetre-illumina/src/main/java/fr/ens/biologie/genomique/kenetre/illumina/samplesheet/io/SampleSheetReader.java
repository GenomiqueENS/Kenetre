/*
 *                  Aozan development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU General Public License version 3 or later 
 * and CeCILL. This should be distributed with the code. If you 
 * do not have a copy, see:
 *
 *      http://www.gnu.org/licenses/gpl-3.0-standalone.html
 *      http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 *
 * Copyright for this code is held jointly by the Genomic platform
 * of the Institut de Biologie de l'École Normale Supérieure and
 * the individual authors. These should be listed in @author doc
 * comments.
 *
 * For more information on the Aozan project and its aims,
 * or to join the Aozan Google group, visit the home page at:
 *
 *      http://outils.genomique.biologie.ens.fr/aozan
 *
 */

package fr.ens.biologie.genomique.kenetre.illumina.samplesheet.io;

import java.io.IOException;

import fr.ens.biologie.genomique.kenetre.illumina.samplesheet.SampleSheet;

/**
 * This interface define a reader for Bcl2fastq samplesheet.
 * @since 1.1
 * @author Laurent Jourdren
 */
public interface SampleSheetReader extends AutoCloseable {

  /**
   * Read a samplesheet.
   * @return a SampleSheet object
   * @throws IOException if an error occurs while reading the samplesheet
   */
  SampleSheet read() throws IOException;

  @Override
  void close() throws IOException;
}
