package hometask;

import com.codeborne.pdftest.PDF;
import com.codeborne.pdftest.matchers.ContainsExactText;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import guru.qa.SelenideDownloadTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.hamcrest.MatcherAssert.assertThat;

public class HomeZipTest {
    ClassLoader cl = SelenideDownloadTest.class.getClassLoader();

    @Test
    void zipParsingPngTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/zip/Robot_Main.zip"));
        try (ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("zip/Robot_Main.zip"))) {
                ZipEntry entry;
                while ((entry = is.getNextEntry()) != null) {
                    org.assertj.core.api.Assertions.assertThat(entry.getName()).isEqualTo("Robot_Main.png");
                    try (InputStream inputStream = zf.getInputStream(entry)) {
                        BufferedImage img = ImageIO.read(inputStream);
                        Assertions.assertThat(img.getHeight()).isEqualTo(512);
                        Assertions.assertThat(img.getWidth()).isEqualTo(512);
                    }
                }
        }
    }

    @Test
    void zipParsingCSVTest () throws Exception{
        ZipFile zf = new ZipFile(new File("src/test/resources/zip/BMONE-796.zip"));
        try (ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("zip/BMONE-796.zip"))) {
            ZipEntry entry;
            while ((entry = is.getNextEntry()) != null) {
                org.assertj.core.api.Assertions.assertThat(entry.getName()).isEqualTo("BMONE-796.csv");
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        List<String[]> content = reader.readAll();
                        Assertions.assertThat(content).contains(
                                new String[] {"Step|Data|Expected Result"}
                        );
                    }
                }
            }
        }
    }

    @Test
    void zipParsingPDFTest () throws Exception{
        ZipFile zf = new ZipFile(new File("src/test/resources/zip/The_Secret_Garden.zip"));
        try (ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("zip/The_Secret_Garden.zip"))) {
            ZipEntry entry;
            while ((entry = is.getNextEntry()) != null) {
                org.assertj.core.api.Assertions.assertThat(entry.getName()).isEqualTo("The_Secret_Garden.pdf");
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    PDF pdf = new PDF(inputStream);
                    Assertions.assertThat(pdf.numberOfPages).isEqualTo(34);
                    Assertions.assertThat(pdf.creator).isEqualTo("Adobe Acrobat 8.11");
                    assertThat(pdf, new ContainsExactText("The robin hopped"));
                }
            }
        }
    }

    @Test
    void zipParsingXLSXTest () throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/zip/CustomFile.zip"));
        try (ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("zip/CustomFile.zip"))) {
            ZipEntry entry;
            while ((entry = is.getNextEntry()) != null) {
                org.assertj.core.api.Assertions.assertThat(entry.getName()).isEqualTo("CustomFile.xlsx");
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    XLS xls = new XLS(inputStream);
                    String stringCellValue = xls.excel.getSheetAt(0).getRow(1).getCell(0).getStringCellValue();
                    org.assertj.core.api.Assertions.assertThat(stringCellValue).contains("AFFILIATES_CODE");
                    int header = xls.excel.getSheetAt(0).getLastRowNum();
                    org.assertj.core.api.Assertions.assertThat(header).isEqualTo(25);
                }
            }
        }
    }
}
