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
 * This interface define a writer for bcl2fastq samplesheet.
 * @since 1.1
 * @author Laurent Jourdren
 */
public interface SampleSheetWriter extends AutoCloseable {

  /**
   * Write a samplesheet.
   * @param samplesheet samplesheet to write
   * @throws IOException if an error occurs while writing the samplesheet
   */
  void writer(SampleSheet samplesheet) throws IOException;

  @Override
  void close() throws IOException;
}
