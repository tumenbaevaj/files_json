package homework;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipParsingTest {

    private ClassLoader cl = ZipParsingTest.class.getClassLoader();

    @Test
    void csvFileParsingTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("homework.zip")
        )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());

                if (entry.getName().endsWith(".csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> data = csvReader.readAll(); //читает CSV файл

                    Assertions.assertArrayEquals(
                            new String[]{"Jibek", "Bishkek"},
                            data.get(1)
                    );

                    return;
                }
            }

            Assertions.fail("CSV файл не найден в архиве");
        }
    }

    @Test
    void pdfFileFromZipParsingTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("homework.zip")
        )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".pdf")) {
                    PDF pdf = new PDF(zis);

                    Assertions.assertTrue(pdf.text.contains("Первый инструмент"));

                    return;
                }
            }

            Assertions.fail("PDF файл не найден в архиве");
        }
    }

    @Test
    void xlsxFileFromZipParsingTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("homework.zip")
        )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".xlsx")) {
                    XLS xls = new XLS(zis);

                    String childHeader = xls.excel.getSheetAt(0)
                            .getRow(2)
                            .getCell(1)
                            .getStringCellValue();

                    String childName = xls.excel.getSheetAt(0)
                            .getRow(3)
                            .getCell(1)
                            .getStringCellValue();

                    String childSurname = xls.excel.getSheetAt(0)
                            .getRow(3)
                            .getCell(2)
                            .getStringCellValue();

                    double amount = xls.excel.getSheetAt(0)
                            .getRow(3)
                            .getCell(7)
                            .getNumericCellValue();

                    Assertions.assertEquals("Ребенок", childHeader);
                    Assertions.assertEquals("Элина", childName);
                    Assertions.assertEquals("Тыныбекова", childSurname);
                    Assertions.assertEquals(2000, amount);

                    return;
                }
            }

            Assertions.fail("XLSX файл не найден в архиве");
        }
    }
}
