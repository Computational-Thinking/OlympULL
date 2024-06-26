package file_management;

import java.io.IOException;
import java.util.ArrayList;

public class FileWriter extends java.io.FileWriter {
    public FileWriter(String fileName, String tableName, ArrayList<String> data) throws IOException {
        super(fileName);

        this.write(tableName + '\n');

        for (String datum : data) {
            this.write(datum + '\n');
        }

    }
}
