package IO;

import Analysis.Result;
import Control.Configuration;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AKWWFormatFileWriter {

    public void printResult(String fileName, Result result, Configuration config) {
        if (config == null) {
            throw new IllegalArgumentException("Recived Result pointer at null.");
        }

        String message;

        if (result == null) {
            if (config.getFromCurrency() == null) {
                message = "The current economic situation does not create any arbitration.";
            } else {
                message = "The cost of the exchange exceed the amount to be exchanged or there is no path to target currency from starting currency.";
            }
        } else {
            message = resultToString(result);
        }

        try (PrintStream output = openStream(fileName, config)) {
            output.print(message);
        }
    }

    private PrintStream openStream(String fileName, Configuration config) {
        try {
            if (fileName == null) {
                DateFormat dateFromat = new SimpleDateFormat("yyyy.MM.dd 'at' HH-mm-ss");
                Date date = new Date();
                fileName = "AKWW - " + config.getInFile() + " " + dateFromat.format(date) + ".txt";
            }

            File outFile = new File(fileName);

            if (outFile.exists()) {
                throw new IllegalArgumentException("File ,," + outFile + "'' already exists. Displaying the result on the console:");
            }

            System.out.print("The result has been saved to the file ,," + outFile + "''.");
            return new PrintStream(new BufferedOutputStream(new FileOutputStream(outFile, true)));
        } catch (IOException e) {
            System.out.println("Could not write into the file ,," + fileName + "''. Displaying the result on the console:");
            return System.out;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return System.out;
        }
    }

    private String resultToString(Result result) {
        if (result == null) {
            throw new IllegalArgumentException("Recived Result pointer at null.");
        }

        StringBuilder sb = new StringBuilder();
        String[] path = result.getPath();
        DecimalFormat df = new DecimalFormat("#.####");

        sb.append("Starting currency -> continuation of exchange -> target currency \n");
        
        String fourDigitsNumber = df.format(result.getStartAmount());
        sb.append(fourDigitsNumber.replace(",", "."));
        sb.append(" ");

        for (String CurrencySymbol : path) {
            sb.append(CurrencySymbol);
            sb.append(" -> ");
        }

        fourDigitsNumber = df.format(result.getEndAmount());
        sb.append(fourDigitsNumber.replace(",", "."));
        sb.append(" ");
        
        sb.append(path[path.length - 1]);

        return sb.toString();
    }

}
