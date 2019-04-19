package Control;

public class Configuration {

    private String inFile;
    private String outFile;
    private String fromCurrency;
    private String toCurrency;
    private double amount;

    public void setInFile(String inFile) {
        this.inFile = inFile;
    }

    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getInFile() {
        return inFile;
    }

    public String getOutFile() {
        return outFile;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getAmount() {
        return amount;
    }
}
