import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {
    private String CSVPath;
    CSVReader(String CSVPath) {
        this.CSVPath = CSVPath;
    }
    CSVParser readRecords() throws IOException {
        CSVParser csvFileParser = CSVFormat.DEFAULT.parse(new FileReader(CSVPath));
        return csvFileParser;
    }
}
