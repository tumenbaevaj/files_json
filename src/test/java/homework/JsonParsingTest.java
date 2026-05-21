package homework;

import com.fasterxml.jackson.databind.ObjectMapper;
import homework.model.Phone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class JsonParsingTest {
    private ClassLoader cl = JsonParsingTest.class.getClassLoader();

    @Test
    void jsonFileParsingTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream is = cl.getResourceAsStream("phone.json")) {
            Phone phone = objectMapper.readValue(is, Phone.class);

            Assertions.assertEquals("Apple", phone.getBrand());
            Assertions.assertEquals("iPhone 15", phone.getModel());
            Assertions.assertEquals(1000, phone.getPrice());
            Assertions.assertTrue(phone.getAvailable());

            Assertions.assertEquals(3, phone.getColors().length);
            Assertions.assertEquals("black", phone.getColors()[0]);
            Assertions.assertEquals("white", phone.getColors()[1]);
            Assertions.assertEquals("gold", phone.getColors()[2]);
        }
    }
}
