package fr.ens.biologie.genomique.kenetre.illumina.interop;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Splitter;

import fr.ens.biologie.genomique.kenetre.KenetreException;

public class ExtendedTileMetricsReaderTest {

  @Test
  public void testNextSeq2000SR100()
      throws URISyntaxException, KenetreException, IOException {

    testReadFile("interop/NextSeq2000_SR100/InterOp");
  }

  @Test
  public void testNextSeq2000PE150()
      throws URISyntaxException, KenetreException, IOException {

    testReadFile("interop/NextSeq2000_PE150/InterOp");
  }

  private void testReadFile(String path)
      throws URISyntaxException, KenetreException, IOException {

    String binFilename = "ExtendedTileMetricsOut.bin";
    String csvFilename = "ExtendedTileMetricsOut.csv";

    // Get the file URL, not working in JAR file.
    URL binResource =
        getClass().getClassLoader().getResource(path + '/' + binFilename);

    if (binResource == null) {
      throw new IllegalArgumentException("file not found!");
    }

    File binFile = new File(binResource.toURI());
    File binDir = binFile.getParentFile();

    // Read metrics from bin file
    List<ExtendedTileMetric> result =
        new ExtendedTileMetricsReader(binDir).readMetrics();

    // Convert result to CSV and sort it
    boolean first = true;
    List<String> binLines = new ArrayList<>();
    List<Class<?>> types = null;
    for (ExtendedTileMetric m : result) {

      if (first) {
        types = m.fieldTypes();
        first = false;
      }

      binLines.add(m.toCSV());
    }
    Collections.sort(binLines);

    // Read expected metrics from csv file and sort it
    List<String> csvLines = new ArrayList<>();
    try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass()
        .getClassLoader().getResourceAsStream(path + '/' + csvFilename)))) {

      String line = null;
      while ((line = in.readLine()) != null) {

        if (!line.startsWith("#") && !line.startsWith("Lane")) {

          csvLines.add(line);
        }
      }
    }
    Collections.sort(csvLines);

    // Check if the same number of entries is found
    Assert.assertEquals(csvLines.size(), binLines.size());

    // Compare values
    Splitter splitter = Splitter.on(',').trimResults();
    int count = csvLines.size();
    for (int i = 0; i < count; i++) {

      // System.out.println(i);
      // System.out.println(csvLines.get(i));
      // System.out.println(binLines.get(i));

      List<String> csvFields =
          splitter.splitToList(csvLines.get(i).replace("nan", "NaN"));
      List<String> binFields =
          splitter.splitToList(binLines.get(i).replace("nan", "NaN"));

      for (int j = 0; j < types.size(); j++) {

        Class<?> c = types.get(j);

        if (c == Float.class) {

          float csvValue = Float.parseFloat(csvFields.get(j));
          float delta = csvValue / 1000f;

          Assert.assertEquals(csvValue, Float.parseFloat(binFields.get(j)),
              delta);
        } else {

          if (!binFields.get(j).equals("-1")) {

            Assert.assertEquals(csvFields.get(j), binFields.get(j));
          }
        }

      }
    }
  }

}
