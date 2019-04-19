package Analysis;

public class Result {

    private String[] path;
    private double startAmount;
    private double endAmount;

    public void setPath(String[] path) {
        this.path = path;
    }

    public void setStartAmount(double startAmount) {
        this.startAmount = startAmount;
    }

    public void setEndAmount(double endAmount) {
        this.endAmount = endAmount;
    }

    public String[] getPath() {
        return path;
    }

    public double getStartAmount() {
        return startAmount;
    }

    public double getEndAmount() {
        return endAmount;
    }

}
