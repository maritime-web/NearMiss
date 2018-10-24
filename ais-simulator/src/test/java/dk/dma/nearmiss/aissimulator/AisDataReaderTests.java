package dk.dma.nearmiss.aissimulator;

import com.opencsv.CSVReader;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
                File file = new ClassPathResource(String.format("%s/%s", dir, fileName)).getFile();
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
    public void forward() {
        while (aisDataReader.forward()) {
            logger.info(aisDataReader.getLine().toString());
        }
    }

    @Test
    @Ignore
    public void name() throws FileNotFoundException, IOException {
        File file = new ClassPathResource("csv/1152DK4201152-20181007-7.csv").getFile();
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            System.out.println(nextLine[0] + nextLine[1] + "etc...");
        }
    }
}
