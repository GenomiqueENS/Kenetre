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

package fr.ens.biologie.genomique.kenetre.bio.readfilter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.ens.biologie.genomique.kenetre.KenetreException;
import fr.ens.biologie.genomique.kenetre.bio.ReadSequence;
import fr.ens.biologie.genomique.kenetre.bio.readfilter.ReadFilter;
import fr.ens.biologie.genomique.kenetre.bio.readfilter.ReadNameRegexReadFilter;

public class ReadNameRegexReadFilterTest {

  @Test
  public void testAcceptReadSequence() throws KenetreException {

    ReadFilter filter = new ReadNameRegexReadFilter();
    filter.setParameter("allowed.regex", "AEGIR");
    filter.init();

    // Null case
    assertFalse(filter.accept(null));

    final String[] ids = {"AEGIR:25:B0866ABXX:8:1101:1193:2125",
        "TOTO:25:B0866ABXX:8:1101:1176:2126",
        "TOTO:25:B0866ABXX:8:1101:1111:4444",
        "TOTO:25:B0866ABXX:7:2202:1176:2126",
        "TOTO:25:B0866ABXX:7:1101:2222:3333"};

    // Not illumina id case
    ReadSequence read = new ReadSequence("read1", "ATG", "wxy");
    assertFalse(filter.accept(read));

    assertTrue(filter.accept(new ReadSequence(ids[0], "", "")));
    assertFalse(filter.accept(new ReadSequence(ids[1], "", "")));
    assertFalse(filter.accept(new ReadSequence(ids[2], "", "")));
    assertFalse(filter.accept(new ReadSequence(ids[3], "", "")));
    assertFalse(filter.accept(new ReadSequence(ids[4], "", "")));

    filter = new ReadNameRegexReadFilter();
    filter.setParameter("allowed.regex", "1176");
    filter.init();

    assertFalse(filter.accept(new ReadSequence(ids[0], "", "")));
    assertTrue(filter.accept(new ReadSequence(ids[1], "", "")));
    assertFalse(filter.accept(new ReadSequence(ids[2], "", "")));
    assertTrue(filter.accept(new ReadSequence(ids[3], "", "")));
    assertFalse(filter.accept(new ReadSequence(ids[4], "", "")));

    filter = new ReadNameRegexReadFilter();
    filter.setParameter("allowed.regex", "B0866ABXX\\:.\\:1101\\:1176");
    filter.init();

    assertFalse(filter.accept(new ReadSequence(ids[0], "", "")));
    assertTrue(filter.accept(new ReadSequence(ids[1], "", "")));
    assertFalse(filter.accept(new ReadSequence(ids[2], "", "")));
    assertFalse(filter.accept(new ReadSequence(ids[3], "", "")));
    assertFalse(filter.accept(new ReadSequence(ids[4], "", "")));

    filter = new ReadNameRegexReadFilter();
    filter.setParameter("forbidden.regex", "B0866ABXX\\:.\\:1101\\:1176");
    filter.init();

    assertTrue(filter.accept(new ReadSequence(ids[0], "", "")));
    assertFalse(filter.accept(new ReadSequence(ids[1], "", "")));
    assertTrue(filter.accept(new ReadSequence(ids[2], "", "")));
    assertTrue(filter.accept(new ReadSequence(ids[3], "", "")));
    assertTrue(filter.accept(new ReadSequence(ids[4], "", "")));

    filter = new ReadNameRegexReadFilter();
    filter.setParameter("allowed.regex", "1101");
    filter.setParameter("forbidden.regex", "AEGIR");
    filter.init();

    assertFalse(filter.accept(new ReadSequence(ids[0], "", "")));
    assertTrue(filter.accept(new ReadSequence(ids[1], "", "")));
    assertTrue(filter.accept(new ReadSequence(ids[2], "", "")));
    assertFalse(filter.accept(new ReadSequence(ids[3], "", "")));
    assertTrue(filter.accept(new ReadSequence(ids[4], "", "")));

  }

}
