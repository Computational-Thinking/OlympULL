package file_management;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class FileReader extends java.io.FileReader {
    public FileReader(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public String readTableName() throws Exception {
        String regexTable = "^[A-Za-z_]+(\\(([A-Za-z_, ]+)\\))?$";

        // Leemos la primera línea
        int currentChar;
        StringBuilder firstLineBuilder = new StringBuilder();
        while ((currentChar = this.read()) != -1 && currentChar != '\n') {
            firstLineBuilder.append((char) currentChar);
        }

        String firstLine = firstLineBuilder.toString();

        if (!firstLine.matches(regexTable)) {
            throw new Exception();
        }

        return firstLine;
    }

    public ArrayList<String> readTableRegisters() throws Exception {
        String regexTuple = "^'[A-Za-z0-9À-ÿÑñ.()¡!:_ -]*'(, '[A-Za-z0-9À-ÿÑñ:.()¡!_ -]*')*$";
        String regexOlympTuple = "^\\('[A-Za-z0-9_ -]*'(, '[A-Za-z0-9_ -]*')*\\)$";

        ArrayList<String> registers = new ArrayList<>();

        int currentChar;
        StringBuilder lineBuilder = new StringBuilder();
        while ((currentChar = this.read()) != -1) {
            if (currentChar == '\n') {
                String line = lineBuilder.toString();
                if (line.matches(regexTuple)) {
                    registers.add(line);
                } else {
                    throw new Exception();
                }
                lineBuilder.setLength(0);
            } else {
                lineBuilder.append((char) currentChar);
            }
        }

        return registers;
    }
}
