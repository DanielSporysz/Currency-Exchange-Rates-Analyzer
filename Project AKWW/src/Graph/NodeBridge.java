package Graph;

import java.util.Objects;

public class NodeBridge {

    private final Node start;
    private final Node end;
    private final double rate;
    private final double fixedCost;
    private final double percentageCost;

    public NodeBridge(Node start, Node end, double rate, double fixedCost, double percentageCost) {
        this.start = start;
        this.end = end;
        this.rate = rate;
        this.fixedCost = fixedCost;
        this.percentageCost = percentageCost;
    }

    public NodeBridge(NodeBridge bridge) {
        this.start = bridge.getStart();
        this.end = bridge.getEnd();
        this.rate = bridge.getRate();
        this.fixedCost = bridge.getFixedCost();
        this.percentageCost = bridge.getPercentageCost();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NodeBridge) {
            return ((NodeBridge) o).getStart().getSymbol().compareTo(start.getSymbol()) == 0
                    && ((NodeBridge) o).getEnd().getSymbol().compareTo(end.getSymbol()) == 0;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;

        hash = 47 * hash + Objects.hashCode(this.start);
        hash = 47 * hash + Objects.hashCode(this.end);
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.rate) ^ (Double.doubleToLongBits(this.rate) >>> 32));
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.fixedCost) ^ (Double.doubleToLongBits(this.fixedCost) >>> 32));
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.percentageCost) ^ (Double.doubleToLongBits(this.percentageCost) >>> 32));

        return hash;
    }

    public double getCost(double previousNodeCost) {
        if (previousNodeCost == Double.MAX_VALUE) {
            return Double.MAX_VALUE;
        }
        return previousNodeCost * rate - previousNodeCost * percentageCost - fixedCost;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public double getRate() {
        return rate;
    }

    public double getFixedCost() {
        return fixedCost;
    }

    public double getPercentageCost() {
        return percentageCost;
    }

}
