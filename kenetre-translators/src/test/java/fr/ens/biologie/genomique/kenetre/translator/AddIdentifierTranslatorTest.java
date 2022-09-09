package fr.ens.biologie.genomique.kenetre.translator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import fr.ens.biologie.genomique.kenetre.translator.AddIdentifierTranslator;
import fr.ens.biologie.genomique.kenetre.translator.MultiColumnTranslator;

public class AddIdentifierTranslatorTest {

  private static final String[] ARRAY_FIELD = {"Col1", "Col2", "Col3", "Col4"};
  private final MultiColumnTranslator transl =
      new MultiColumnTranslator(ARRAY_FIELD);

  @Test
  public void testAddIdentifierTranslator() {

    try {

      new AddIdentifierTranslator(null);
      fail();
    } catch (RuntimeException e) {

      assertTrue(true);
    }

    AddIdentifierTranslator addIdTransl = new AddIdentifierTranslator(transl);
    List<String> fields = addIdTransl.getFields();
    assertEquals("OriginalId", fields.get(0));
  }

  @Test
  public void testSetGetUpdateFields() {

    AddIdentifierTranslator addIdTransl =
        new AddIdentifierTranslator(transl, "first field");
    addIdTransl.setNewFieldName("last field");
    List<String> fields = addIdTransl.getFields();
    assertEquals("first field", fields.get(0));
    assertEquals("Col2", fields.get(1));
    assertEquals("Col3", fields.get(2));
    assertEquals("Col4", fields.get(3));

    addIdTransl.updateFields();

    fields = addIdTransl.getFields();
    assertEquals("last field", fields.get(0));
    assertEquals("Col2", fields.get(1));
    assertEquals("Col3", fields.get(2));
    assertEquals("Col4", fields.get(3));

  }

}
