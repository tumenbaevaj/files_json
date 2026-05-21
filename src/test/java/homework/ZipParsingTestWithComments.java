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

public class ZipParsingTestWithComments {

    private ClassLoader cl = ZipParsingTestWithComments.class.getClassLoader();
    // помогает доставать файлы из папки resources,
    // cl.getResourceAsStream("homework.zip") - Java ищет файл в src/test/resources/homework.zip
    @Test
    void zipFileParsingTest() throws Exception {
        // try (...) { } - после окончания теста Java сама закроет файл
        // ZipInputStream - поток для чтения zip-архив
        // throws Exception - если будет ошибка при чтении файла, Java может её выбросить
        // new ZipInputStream(...) - Java, читай файл как архив
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("homework.zip")
        )) {
            ZipEntry entry; // создание переменной для текущего файла внутри архива

            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());

                if (entry.getName().endsWith(".csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    // создание CSVReader'а
                    // InputStreamReader(zis) - переводит поток в текстовый вид
                    List<String[]> data = csvReader.readAll(); //читает CSV файл

                    Assertions.assertArrayEquals(
                            new String[]{"Jibek", "Bishkek"},
                            data.get(1)
                    );
                }

                if (entry.getName().endsWith(".pdf")) {
                    PDF pdf = new PDF(zis);

                    Assertions.assertTrue(pdf.text.contains("Первый инструмент"));
                }

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
                    Assertions.assertEquals("Элина", childName.trim());
                    Assertions.assertEquals("Тыныбекова", childSurname);
                    Assertions.assertEquals(2000, amount);
                }
            }
        }
    }
}


