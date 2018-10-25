package dk.dma.nearmiss.aissimulator;

import com.opencsv.CSVReader;
import dk.dma.ais.proprietary.GatehouseFactory;
import dk.dma.ais.proprietary.GatehouseSourceTag;
import dk.dma.ais.sentence.SentenceLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AisDataReaderTests {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AisDataReader aisDataReader;

    @Test
    public void testDirectoryConfiguration() {
        AisDataReaderConfiguration conf = aisDataReader.getConfiguration();
        assertNotNull("Expected directory defined", conf.getDirectory());
        assertEquals("Expected right directory name", "csv", conf.getDirectory());
    }


    @Test
    public void testColumnsConfiguration() {
        AisDataReaderConfiguration conf = aisDataReader.getConfiguration();
        assertNotNull("Expected columns defined", conf.getColumns());
        assertEquals("Expected two columns defined", 2, conf.getColumns().size());
        List<String> expectedColumns = Arrays.asList("NMEA message", "PGHP time stamp");
        assertTrue("Expected correct columns", conf.getColumns().containsAll(expectedColumns));
    }

    @Test
    public void testFilesConfiguration() {
        AisDataReaderConfiguration conf = aisDataReader.getConfiguration();
        assertNotNull("Expected files defined", conf.getFiles());
        assertTrue("Expected at least one file given", conf.getFiles().size() > 0);
    }

    @Test
    public void testConfiguredFilesExist() {
        AisDataReaderConfiguration conf = aisDataReader.getConfiguration();
        String dir = conf.getDirectory();
        List<String> fileNames = conf.getFiles();
        boolean allFilesFound = true;

        for (String fileName : fileNames) {
            try {
                new ClassPathResource(String.format("%s/%s", dir, fileName)).getFile();
            } catch (IOException e) {
                allFilesFound = false;
                logger.error(String.format("File: %s not found in the %s directory.", fileName, dir));
            }
        }
        assertTrue("Expected all files to be found", allFilesFound);
    }

    @Test
    public void testHeaderInFile() {
        AisDataReaderConfiguration conf = aisDataReader.getConfiguration();
        String dir = conf.getDirectory();
        List<String> fileNames = conf.getFiles();
        boolean allFilesFound = true;
        boolean errorsFound = false;

        for (String fileName : fileNames) {
            try {
                File file = new ClassPathResource(String.format("%s/%s", dir, fileName)).getFile();
                CSVReader reader = new CSVReader(new FileReader(file));
                String[] headerLine = reader.readNext();

                if (conf.getColumns().size() != headerLine.length) {
                    logger.error(String.format("File: %s has wrong number of columns.", fileName));
                    errorsFound = true;
                } else {
                    List<String> foundColumns = Arrays.asList(headerLine[0], headerLine[1]);
                    if (!foundColumns.containsAll(conf.getColumns())) {
                        logger.error(String.format("File: %s has wrong column names.", fileName));
                        errorsFound = true;
                    }
                }
            } catch (IOException e) {
                allFilesFound = false;
                logger.error(String.format("File: %s not found in the %s directory.", fileName, dir));
            }
        }

        assumeTrue("All files should be found", allFilesFound);
        assertTrue("Expected correct columns defined in files.", !errorsFound);
    }


    @Test
    public void testForward() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));

        while (aisDataReader.forward()) {
            AisDataLine aisDataLine = aisDataReader.getLine();

            SentenceLine sentenceLine = new SentenceLine(aisDataLine.getTime());
            GatehouseSourceTag tag = (GatehouseSourceTag) GatehouseFactory.parseTag(sentenceLine);

            Instant instant = tag.getTimestamp().toInstant();
            String timestamp = formatter.format(instant);

            logger.info(String.format("Timestamp: %s Message: %s", timestamp, aisDataReader.getLine().getMessage()));
        }
    }

}
