package IO;

import Control.Configuration;
import Graph.Node;
import java.util.HashMap;

public class ArgumentsParser {

    public Configuration parseArguments(String[] args) throws IllegalConfigurationArgumentsException {
        if (args == null || args.length < 2) {
            throw new IllegalConfigurationArgumentsException("Too few arguments given. No work has been done.");
        }

        Configuration config = new Configuration();

        config.setInFile(args[0]);

        try {
            config.setAmount(Double.parseDouble(args[1]));
            if (config.getAmount() <= 0) {
                throw new IllegalConfigurationArgumentsException("The amount number has to be positive number greater than zero. No work has been done.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalConfigurationArgumentsException("Given argument ,," + args[1] + "'' is not a number.");
        }

        switch (args.length) {
            case 2:
            case 3:
                config.setFromCurrency(null);
                config.setToCurrency(null);
                config.setOutFile(args.length > 2 ? args[2] : null);
                break;
            case 4:
            case 5:
                config.setFromCurrency(args[2]);
                config.setToCurrency(args[3]);
                config.setOutFile(args.length > 4 ? args[4] : null);
                break;
            default:
                throw new IllegalConfigurationArgumentsException("Too many arguments given. No work has been done.");
        }

        return config;
    }

    public void validateArguments(Configuration config, HashMap<String, Node> nodes) throws IllegalConfigurationArgumentsException, IllegalInputFileException {
        if (config == null || nodes == null) {
            throw new IllegalArgumentException("Fields Configuration or HashMap<String, Node> were not initialized.");
        }
        
        if (config.getFromCurrency() != null && !nodes.containsKey(config.getFromCurrency())) {
            throw new IllegalInputFileException("Starting currency ,," + config.getFromCurrency() + "'' is not defined in file ,," + config.getInFile() + "''.");
        }
        if (config.getToCurrency() != null && !nodes.containsKey(config.getToCurrency())) {
            throw new IllegalInputFileException("Target currency ,," + config.getToCurrency() + "'' is not defined in the file ,," + config.getInFile() + "''.");
        }
        if (config.getAmount() <= 0) {
            throw new IllegalConfigurationArgumentsException("Given amount of money is less than zero.");
        }
        if (config.getOutFile() != null && config.getInFile().compareTo(config.getOutFile()) == 0) {
            throw new IllegalConfigurationArgumentsException("Given output file name is the same as the input file name.");
        }
    }

    public void printHelpMenu() {
        System.out.println("AKWW program accepts arguments in the following formats:");
        System.out.println("Searching for an economic arbitration");
        System.out.println("\t[data filename] [amount to exchange]");
        System.out.println("\t[data filename] [amount to exchange] [output filename]");
        System.out.println("Searching for the most favorable exchange path.");
        System.out.println("\t[data filename] [amount to exchange] [starting currency] [target currency]");
        System.out.print("\t[data filename] [amount to exchange] [starting currency] [target currency] [output filename]");
    }

}
