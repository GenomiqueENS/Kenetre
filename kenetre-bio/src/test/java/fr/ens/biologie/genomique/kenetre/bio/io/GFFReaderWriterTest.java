package fr.ens.biologie.genomique.kenetre.bio.io;

import static fr.ens.biologie.genomique.kenetre.util.StringUtils.md5DigestToString;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import fr.ens.biologie.genomique.kenetre.bio.GFFEntry;

public class GFFReaderWriterTest {

  @Test
  public void testReadWrite() throws IOException, NoSuchAlgorithmException {

    testFile("/example.gff3");
  }

  private void testFile(final String resourcePath)
      throws NoSuchAlgorithmException, IOException {

    MessageDigest mdi = MessageDigest.getInstance("MD5");
    MessageDigest mdo = MessageDigest.getInstance("MD5");

    InputStream resourceStream =
        this.getClass().getResourceAsStream(resourcePath);

    if (resourceStream == null) {
      throw new IOException("resource not found: " + resourcePath);
    }

    try (InputStream is = resourceStream;
        OutputStream os = OutputStream.nullOutputStream();
        DigestInputStream dis = new DigestInputStream(is, mdi);
        DigestOutputStream dos = new DigestOutputStream(os, mdo);
        GFFReader reader = new GFFReader(dis);
        GFFWriter writer = new GFFWriter(dos)) {

      for (GFFEntry e : reader) {
        writer.write(e);
      }
    }

    assertEquals(md5DigestToString(mdi), md5DigestToString(mdo));
  }

}
