package dk.dma.nearmiss.aissimulator;

import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public final class AisDataReader {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AisDataReaderConfiguration configuration;

    private Integer currentFileIndex;
    private CSVReader csvReader;
    private AisDataLine currentLine;
    private boolean noMoreMessages = false;

    public AisDataReader(AisDataReaderConfiguration configuration) {
        this.configuration = configuration;
    }

    AisDataReaderConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Returns current AIS data line.
     *
     * @return the current available AIA line.
     */
    AisDataLine getLine() {
        return currentLine;
    }

    /**
     * Forwards to next AIS data line.
     *
     * @return Another line found (false if no more lines).
     */
    boolean forward() {
        currentLine = null;
        if (noMoreMessages) throw new IllegalStateException("No More AIS Messages was signalled earlier!");

        String[] line = null;

        try {
            if (csvReader == null) {
                csvReader = nextCsvReader();
            }

            while (line == null && csvReader != null) {
                line = csvReader.readNext();
                if (line == null) {
                    csvReader = nextCsvReader();
                }
            }
        } catch (IOException e) {
            logger.error(String.format("IO error encountered during forward (%s)", e.getMessage()));
            logger.error("Stopping read of AIS input.");
        }

        if (line != null) {
            currentLine = new AisDataLine(line[0], line[1]);
            return true;
        }

        else {
            currentLine = null;
            noMoreMessages = true;
            return false;
        }

    }

    /*
     * Optains a CSV reader for the next file.
     */
    private CSVReader nextCsvReader() throws IOException {
        currentFileIndex = nextFileIndex(currentFileIndex, configuration.getFiles());
        if (currentFileIndex == null) {
            noMoreMessages = true;
            return null;
        }

        String fileName = configuration.getFiles().get(currentFileIndex);
        InputStream isr = new ClassPathResource(String.format("%s/%s", configuration.getDirectory(), fileName)).getInputStream();
        csvReader = new CSVReader(new InputStreamReader(isr));

        String[] headerLine = csvReader.readNext();
        if (headerLine != null) {
            logger.debug(String.format("Number of columns in new file: %s", headerLine.length));
        } else {
            logger.error("Heading expected file seems to be empty");
            logger.error("Stopping read of AIS input.");
            noMoreMessages = true;
            return null;
        }
        return csvReader;
    }

    /*
     * This method counts the current file index up each time it is called.
     * First time called it will return 0.
     * When index value gets bigger than highest possible value the method will return null.
     */
    private Integer nextFileIndex(Integer currentFileIndex, List<String> fileNames) {
        if (fileNames == null) return null;
        if (fileNames.isEmpty()) return null;
        if (currentFileIndex == null) return 0;

        Integer nextFileIndex = currentFileIndex + 1;

        if (nextFileIndex < fileNames.size())
            return nextFileIndex;
        else
            return null;
    }


}
