package IO;

import Control.Configuration;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

public class ArgumentsParserTest {

    private final ArgumentsParser instance;
    private final double doublesCompareUncertainty = 0.0001;

    public ArgumentsParserTest() {
        instance = new ArgumentsParser();
    }

    @Test
    public void testParseArguments() {
        testParseArgumentNull();
        testParseArgumentTooFewArguments();
        testParseArgumentTooManyArguments();
        testParseArgumentArbitration();
        testParseArgumentArbitrationWithOutFileSpecified();
        testParseArgumentBestPath();
        testParseArgumentBestPathWithOutFileSpecified();
        testParseArgumentNotANumberArgument();
    }

    private void testParseArgumentBestPathWithOutFileSpecified() {
        //given
        Configuration conf;
        String[] args = {"FileName1", "1234", "CURR1", "CURR2", "FileName2"};

        //when
        String inFile = args[0];
        Double amount = Double.parseDouble(args[1]);
        String curr1 = args[2];
        String curr2 = args[3];
        String outFile = args[4];

        //then
        try {
            conf = instance.parseArguments(args);
            Assert.assertEquals(amount, conf.getAmount(), doublesCompareUncertainty);
            Assert.assertEquals(inFile, conf.getInFile());
            Assert.assertEquals(curr1, conf.getFromCurrency());
            Assert.assertEquals(curr2, conf.getToCurrency());
            Assert.assertEquals(outFile, conf.getOutFile());
        } catch (IllegalConfigurationArgumentsException e) {
            fail();
        }
    }

    private void testParseArgumentBestPath() {
        //given
        Configuration conf;
        String[] args = {"FileName", "1234", "CURR1", "CURR2"};

        //when
        String inFile = args[0];
        Double amount = Double.parseDouble(args[1]);
        String curr1 = args[2];
        String curr2 = args[3];

        //then
        try {
            conf = instance.parseArguments(args);
            Assert.assertEquals(amount, conf.getAmount(), doublesCompareUncertainty);
            Assert.assertEquals(inFile, conf.getInFile());
            Assert.assertEquals(curr1, conf.getFromCurrency());
            Assert.assertEquals(curr2, conf.getToCurrency());
        } catch (IllegalConfigurationArgumentsException e) {
            fail();
        }
    }

    private void testParseArgumentArbitrationWithOutFileSpecified() {
        //given
        Configuration conf;
        String[] args = {"FileName1", "1234", "FileName2"};

        //when
        String inFile = args[0];
        Double amount = Double.parseDouble(args[1]);
        String outFile = args[2];

        //then
        try {
            conf = instance.parseArguments(args);
            Assert.assertEquals(amount, conf.getAmount(), doublesCompareUncertainty);
            Assert.assertEquals(inFile, conf.getInFile());
            Assert.assertEquals(outFile, conf.getOutFile());
        } catch (IllegalConfigurationArgumentsException e) {
            fail();
        }
    }

    private void testParseArgumentArbitration() {
        //given
        Configuration conf;
        String[] args = {"FileName", "1234"};

        //when
        String inFile = args[0];
        Double amount = Double.parseDouble(args[1]);

        //then
        try {
            conf = instance.parseArguments(args);
            Assert.assertEquals(amount, conf.getAmount(), doublesCompareUncertainty);
            Assert.assertEquals(inFile, conf.getInFile());
        } catch (IllegalConfigurationArgumentsException e) {
            fail();
        }
    }

    private void testParseArgumentTooManyArguments() {
        //given
        String[] args = {"FileName", "1234", "CURRENCY1", "CURRENCY2", "FileName2", "extraArgument"};

        //then
        try {
            instance.parseArguments(args);
        } catch (IllegalConfigurationArgumentsException e) {
            return;
        }
        fail();
    }

    private void testParseArgumentTooFewArguments() {
        //given
        String[] args = {"FileName"};

        //then
        try {
            instance.parseArguments(args);
        } catch (IllegalConfigurationArgumentsException e) {
            return;
        }
        fail();
    }

    private void testParseArgumentNull() {
        //given
        String[] args = null;
        try {

            //then
            instance.parseArguments(args);
        } catch (IllegalConfigurationArgumentsException e) {
            return;
        }
        fail();
    }

    private void testParseArgumentNotANumberArgument() {
        //given
        Configuration conf;
        String[] args = {"FileName1", "123za23", "CURR1", "CURR2", "FileName2"};

        //then
        try {
            conf = instance.parseArguments(args);
        } catch (IllegalConfigurationArgumentsException e) {
            return;
        }
        fail();
    }

}
