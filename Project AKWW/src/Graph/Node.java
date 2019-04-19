package Graph;

import java.util.ArrayList;
import java.util.Iterator;

public class Node implements Iterable<NodeBridge>, Comparable<Node> {

    private final String symbol;
    private final String fullName;
    private final ArrayList<NodeBridge> bridges;

    public Node(String symbol, String fullName) {
        this.symbol = symbol;
        this.fullName = fullName;
        bridges = new ArrayList<>();
    }

    public Node(Node node) {
        symbol = node.getSymbol();
        fullName = node.getFullName();
        bridges = new ArrayList<>();
        for (NodeBridge bridge : node) {
            bridges.add(new NodeBridge(bridge));
        }
    }

    @Override
    public int compareTo(Node o) {
        return symbol.compareTo(o.getSymbol());
    }

    @Override
    public Iterator<NodeBridge> iterator() {
        return bridges.iterator();
    }

    public void removeBridgeTo(Node end) {
        NodeBridge toRemove = null;
        for (NodeBridge bridge : this) {
            if (bridge.getEnd().getSymbol().compareTo(end.getSymbol()) == 0) {
                toRemove = bridge;
                break;
            }
        }
        if (toRemove != null) {
            bridges.remove(toRemove);
        }
    }

    public NodeBridge getBridgeTo(Node end) {
        for (NodeBridge bridge : this) {
            if (bridge.getEnd().getSymbol().compareTo(end.getSymbol()) == 0) {
                return bridge;
            }
        }
        return null;
    }

    public void addBridge(Node end, double rate, double fixedCost, double percentageCost) {
        bridges.add(new NodeBridge(this, end, rate, fixedCost, percentageCost));
    }

    public ArrayList<NodeBridge> getBridges() {
        return bridges;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getFullName() {
        return fullName;
    }

}
