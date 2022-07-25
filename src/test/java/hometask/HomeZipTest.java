package hometask;

import guru.qa.SelenideDownloadTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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
}
