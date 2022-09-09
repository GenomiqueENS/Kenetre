package fr.ens.biologie.genomique.kenetre.bio.readfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.ens.biologie.genomique.kenetre.KenetreException;
import fr.ens.biologie.genomique.kenetre.bio.ReadSequence;
import fr.ens.biologie.genomique.kenetre.bio.readfilter.LeadingTrimmerReadFilter;
import fr.ens.biologie.genomique.kenetre.bio.readfilter.ReadFilter;

public class LeadingTrimmerReadFilterTest {
  @Test
  public void LeadingTirmmertest() throws KenetreException {
    ReadFilter filter = new LeadingTrimmerReadFilter();

    filter.setParameter("arguments", "33");
    filter.init();

    ReadSequence read = new ReadSequence("read1", "AGG", "ABC");
    assertTrue(filter.accept(read));
    assertEquals("read1", read.getName());
    assertEquals("GG", read.getSequence());
    assertEquals("BC", read.getQuality());
    assertFalse(filter.accept(null));

    filter = new LeadingTrimmerReadFilter();
    filter.setParameter("arguments", "34");
    filter.init();
    assertTrue(filter.accept(read));
    assertEquals("read1", read.getName());
    assertEquals("G", read.getSequence());
    assertEquals("C", read.getQuality());
    assertFalse(filter.accept(null));

    filter = new LeadingTrimmerReadFilter();
    filter.setParameter("arguments", "35");
    filter.init();
    assertFalse(filter.accept(read));
    assertEquals("read1", read.getName());
    assertEquals("G", read.getSequence());
    assertEquals("C", read.getQuality());
    assertFalse(filter.accept(null));

    filter = new LeadingTrimmerReadFilter();
    filter.setParameter("arguments", "34");
    filter.init();
    read = new ReadSequence("read2", "AGAGTTA", "CABABAA");
    assertTrue(filter.accept(read));
    assertEquals("read2", read.getName());
    assertEquals("AGAGTTA", read.getSequence());
    assertEquals("CABABAA", read.getQuality());

    read = new ReadSequence("read3", "AGAGT", "ABABC");
    assertTrue(filter.accept(read));
    assertEquals("read3", read.getName());
    assertEquals("T", read.getSequence());
    assertEquals("C", read.getQuality());

  }
}
