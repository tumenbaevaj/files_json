package classwork;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class SelenideFilesTest {

    @Test
    void downloadFileTest() throws Exception {
        open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloaded =
                $(".react-blob-header-edit-and-raw-actions [href*='main/README.md']")
                        .download();

        /* показывает скачанный файл
        InputStream is = new FileInputStream(downloaded);
        System.out.println();*/

        /* в старой версии Java закрывать за собой следующим образом:
        InputStream is = null;
        try {
            is = new FileInputStream(downloaded);
            // smt//
        } finally {
            if (is !=null) {
                is.close();
            }
        }*/

        /*новая языковая конструкция try with resources, то как работает под капотом:

        try(InputStream is = new FileInputStream(downloaded)) {
            byte[] data = is.readAllBytes();
            String dataAsString = new String(data, StandardCharsets.UTF_8);
            Assertions.assertTrue(dataAsString.contains("Contributions to JUnit 5 are both welcomed and appreciated"));
        }
        // тоже самое, что и:

        String dataAsString = FileUtils.readFileToString(downloaded, StandardCharsets.UTF_8);*/

        try(InputStream is = new FileInputStream(downloaded)) {
            byte[] data = is.readAllBytes();
            String dataAsString = new String(data, StandardCharsets.UTF_8);
            Assertions.assertTrue(dataAsString.contains("Contributions to JUnit are both welcomed and appreciated"));
        }

        /*proxy mode скачивания:
        static {
            Configuration.fileDownload = FileDownloadMode.PROXY;
        }*/
    }

    @Test
    void uploadFileTest() {
        open("https://demoqa.com/upload-download");
        $("input[type='file']").uploadFromClasspath("birthday_card.png");
        $("#uploadedFilePath").shouldHave(text("birthday_card.png"));
    }
}

