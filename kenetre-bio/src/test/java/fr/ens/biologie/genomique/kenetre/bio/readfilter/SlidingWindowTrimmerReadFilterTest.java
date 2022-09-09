package fr.ens.biologie.genomique.kenetre.bio.readfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.ens.biologie.genomique.kenetre.KenetreException;
import fr.ens.biologie.genomique.kenetre.bio.ReadSequence;
import fr.ens.biologie.genomique.kenetre.bio.readfilter.ReadFilter;
import fr.ens.biologie.genomique.kenetre.bio.readfilter.SlidingWindowTrimmerReadFilter;

public class SlidingWindowTrimmerReadFilterTest {
  @Test
  public void SlidingWindowTirmmertest() throws KenetreException {
    ReadFilter filter = new SlidingWindowTrimmerReadFilter();
    filter.setParameter("arguments", "4:29");
    filter.init();
    assertFalse(filter.accept(null));
    ReadSequence read = new ReadSequence("read1", "AGGT", "AA;;");
    assertTrue(filter.accept(read));
    assertEquals("read1", read.getName());
    assertEquals("AG", read.getSequence());
    assertEquals("AA", read.getQuality());

    filter = new SlidingWindowTrimmerReadFilter();
    filter.setParameter("arguments", "6:29");
    filter.init();
    read = new ReadSequence("read2", "ATCTGGT", "A;;AA;;");
    assertTrue(filter.accept(read));
    assertEquals("read2", read.getName());
    assertEquals("ATCTG", read.getSequence());
    assertEquals("A;;AA", read.getQuality());

    filter = new SlidingWindowTrimmerReadFilter();
    filter.setParameter("arguments", "9:27.3");
    filter.init();
    read = new ReadSequence("read3", "ATATCTGGT", ";;A;;AA;;");
    assertTrue(filter.accept(read));
    assertEquals("read3", read.getName());
    assertEquals("ATATCTG", read.getSequence());
    assertEquals(";;A;;AA", read.getQuality());

    filter = new SlidingWindowTrimmerReadFilter();
    filter.setParameter("arguments", "9:27");
    filter.init();
    read = new ReadSequence("read3", "ATATCTGGT", "AA;;;;;;;");
    assertTrue(filter.accept(read));
    assertEquals("read3", read.getName());
    assertEquals("AT", read.getSequence());
    assertEquals("AA", read.getQuality());

    read = new ReadSequence("read3", "ATATCTGGT", ";;;;;;;AA");
    assertTrue(filter.accept(read));
    assertEquals("read3", read.getName());
    assertEquals("ATATCTGGT", read.getSequence());
    assertEquals(";;;;;;;AA", read.getQuality());

  }
}
