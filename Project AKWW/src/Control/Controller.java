package Control;

import Analysis.Analyser;
import Analysis.Result;
import Graph.Node;
import IO.ArgumentsParser;
import IO.AKWWFormatFileReader;
import IO.AKWWFormatFileWriter;
import IO.IllegalConfigurationArgumentsException;
import IO.IllegalInputFileException;
import java.util.HashMap;

public class Controller {

    private Configuration config;
    private HashMap<String, Node> nodes;
    private Result result;

    public static void main(String[] args) {
        Controller controller = new Controller();

        try {
            controller.readArguments(args);
            controller.loadData();
            controller.validateArguments();
            controller.runAnalysis();
            controller.saveResult();
        } catch (IllegalConfigurationArgumentsException e) {
            System.out.println(e.getMessage());
            new ArgumentsParser().printHelpMenu();
        } catch (IllegalInputFileException e) {
            System.out.print(e.getMessage());
        } catch (Exception e) {
            System.out.print("An error occurred while the program was running (" + e.getMessage() + ")");
        }
    }

    private void readArguments(String[] args) throws IllegalConfigurationArgumentsException {
        ArgumentsParser parser = new ArgumentsParser();
        config = parser.parseArguments(args);
    }

    private void loadData() throws IllegalInputFileException {
        AKWWFormatFileReader reader = new AKWWFormatFileReader();
        nodes = reader.readFile(config.getInFile());
    }

    private void validateArguments() throws IllegalConfigurationArgumentsException, IllegalInputFileException {
        ArgumentsParser parser = new ArgumentsParser();
        parser.validateArguments(config, nodes);
    }

    private void runAnalysis() {
        Analyser analyser = new Analyser();
        result = analyser.analyze(config, nodes);
    }

    private void saveResult() {
        AKWWFormatFileWriter writer = new AKWWFormatFileWriter();
        writer.printResult(config.getOutFile(), result, config);
    }

}
