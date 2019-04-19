package IO;

import Graph.Node;
import Graph.NodeBridge;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class AKWWFormatFileReader {

    private HashMap<String, Node> map;
    private int lineCounter;
    private boolean partDefiningNames;
    private boolean partDefiningDependency;

    public HashMap<String, Node> readFile(String inFile) throws IllegalInputFileException {
        if (inFile == null) {
            throw new NullPointerException("Recived String pointer at null.");
        }

        try {
            map = new HashMap<>();
            lineCounter = 0;
            partDefiningNames = false;
            partDefiningDependency = false;

            try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    analyzeLine(line);
                }
            }

            return map;
        } catch (IOException e) {
            throw new IllegalInputFileException("File ,," + inFile + "'' does not exsists or cannot be read.");
        } catch (IllegalInputFileException e) {
            throw e;
        }
    }

    private void analyzeLine(String line) throws IllegalInputFileException {
        lineCounter++;

        if (line == null) {
            throw new NullPointerException("Recived String pointer at null.");
        }

        /* Controlling which part of the file is being read */
        if (line.startsWith("#")) {
            if (partDefiningNames == false || partDefiningNames == false) {
                partDefiningNames = true;
            } else if (partDefiningNames == true) {
                partDefiningNames = false;
                partDefiningDependency = true;
            } else if (partDefiningDependency = true) {
                throw reportWrongDataFromat();
            }
            return;
        }

        if (partDefiningNames) {
            readDefinition(line);
        } else if (partDefiningDependency) {
            readDependancy(line);
        } else {
            throw reportWrongDataFromat();
        }
    }

    private void readDependancy(String line) throws IllegalInputFileException {
        if (line == null) {
            throw new IllegalArgumentException("Recived String pointer at null.");
        }

        String[] tokens = line.split(" ");

        if (tokens.length != 6) {
            throw reportIncorretLine();
        }

        String fromCurrency = tokens[1];
        String toCurrency = tokens[2];
        try {
            tokens[3] = tokens[3].replace(',', '.');
            tokens[5] = tokens[5].replace(',', '.');
            double rate = Double.parseDouble(tokens[3]);
            double cost = Double.parseDouble(tokens[5]);

            switch (tokens[4].substring(0, 3)) {
                case "STA":
                    putDependancy(fromCurrency, toCurrency, rate, cost, 0d);
                    break;
                case "PRO":
                    putDependancy(fromCurrency, toCurrency, rate, 0d, cost);
                    break;
                default:
                    throw reportIncorretLine();
            }
        } catch (NumberFormatException e) {
            throw reportIncorretLine();
        }
    }

    private void putDependancy(String start, String end, double rate, double fixedCost, double percentageCost) throws IllegalInputFileException {
        Node startNode = map.get(start);
        Node endNode = map.get(end);

        if (startNode == null || endNode == null) {
            throw reportMissingDefinition();
        }

        for (NodeBridge bridge : startNode) {
            if (bridge.getStart().compareTo(startNode) == 0 && bridge.getEnd().compareTo(endNode) == 0) {
                throw reportDoubledDependancy();
            }
        }
        startNode.addBridge(endNode, rate, fixedCost, percentageCost);
    }

    private void readDefinition(String line) throws IllegalInputFileException {
        if (line == null) {
            throw new IllegalArgumentException("Recived String pointer at null.");
        }

        String[] tokens = line.split(" ");

        if (tokens.length < 3) {
            throw reportIncorretLine();
        }

        String symbol = tokens[1];
        String fullName = line.substring(tokens[0].length() + tokens[1].length() + 2, line.length());

        putDefiniition(symbol, fullName);
    }

    private void putDefiniition(String symbol, String fullName) throws IllegalInputFileException {
        if (symbol == null && fullName == null) {
            throw new IllegalArgumentException("Recived String pointer at null.");
        }

        if (map.containsKey(symbol)) {
            throw reportDoubledDefinition();
        }

        Node node = new Node(symbol, fullName);
        map.put(symbol, node);
    }

    private IllegalInputFileException reportWrongDataFromat() {
        return new IllegalInputFileException("Data file format in given file is not ,,AKWW file format''.");
    }

    private IllegalInputFileException reportDoubledDependancy() {
        return new IllegalInputFileException("Line " + lineCounter + " contains already defined currency exachnge path.");
    }

    private IllegalInputFileException reportMissingDefinition() {
        return new IllegalInputFileException("Line " + lineCounter + " contains undefined currency.");
    }

    private IllegalInputFileException reportIncorretLine() {
        return new IllegalInputFileException("Line " + lineCounter + " contains unrecognisable data.");
    }

    private IllegalInputFileException reportDoubledDefinition() {
        return new IllegalInputFileException("Line " + lineCounter + " contains already defined currency.");
    }
}
