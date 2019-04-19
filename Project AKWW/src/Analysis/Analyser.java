package Analysis;

import Control.Configuration;
import Graph.Node;
import Graph.NodeBridge;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Analyser {

    public Result analyze(Configuration config, HashMap<String, Node> nodes) {
        if (config == null || nodes == null) {
            throw new IllegalArgumentException("Recived Configuration pointer or/and HashMap<String, Node> pointer at null.");
        }

        if (config.getFromCurrency() != null) {
            return findBestExchangeRoute(config, nodes);
        } else {
            return findArbitration(config, nodes);
        }
    }

    private Result findBestExchangeRoute(Configuration config, HashMap<String, Node> nodes) {
        if (config == null || nodes == null) {
            throw new NullPointerException("Recived Configuration point at null or/and recived HashMap<String, Node> pointer at null.");
        }
        if (config.getFromCurrency() == null || config.getToCurrency() == null) {
            throw new NullPointerException("The fields of Configuration have not been initialized.");
        }

        HashMap<String, Node> copyOfNodes = cloneMapAndNodes(nodes);

        /*removing all connections TO the starting currency node */
        if (config.getFromCurrency().compareTo(config.getToCurrency()) != 0) {
            for (Node node : copyOfNodes.values()) {
                node.removeBridgeTo(copyOfNodes.get(config.getFromCurrency()));
                copyOfNodes.get(config.getToCurrency()).removeBridgeTo(node);
            }
        }

        Stack<Result> results = new Stack<>();
        Stack<HashMap<String, Node>> nodesSet = new Stack<>();

        HashMap<String, Node> previous;
        HashMap<String, Double> cost;

        nodesSet.push(cloneMapAndNodes(copyOfNodes));

        while (!nodesSet.isEmpty()) {
            HashMap<String, Node> tmpNodes = nodesSet.pop();

            previous = getMapOfPrevious(tmpNodes);
            cost = getMapOfCost(config, tmpNodes);

            analyze(tmpNodes, previous, cost);

            try {
                Result result = results.push(convertHashMapsIntoResult(tmpNodes, previous, cost, nodesSet, config));
                results.push(result);
            } catch (LoopException e) {
            }
        }

        Result bestResult = results.pop();
        while (!results.isEmpty()) {
            Result tmp = results.pop();
            if (tmp != null && tmp.getEndAmount() > bestResult.getEndAmount()) {
                bestResult = tmp;
            }
        }

        return correctResult(bestResult, nodes, config);
    }

    private Result findArbitration(Configuration config, HashMap<String, Node> nodes) {
        if (config == null || nodes == null) {
            throw new NullPointerException("Recived Configuration point at null or/and recived HashMap<String, Node> pointer at null.");
        }

        Stack<Result> results = new Stack<>();
        Configuration configForArbitration = new Configuration();
        configForArbitration.setAmount(config.getAmount());

        for (Node node : nodes.values()) {
            configForArbitration.setToCurrency(node.getSymbol());
            configForArbitration.setFromCurrency(node.getSymbol());

            results.push(findBestExchangeRoute(configForArbitration, nodes));
        }

        Result bestResult = results.pop();
        while (!results.isEmpty()) {
            Result tmpResult = results.pop();
            if (bestResult == null) {
                bestResult = tmpResult;
            } else if (tmpResult != null && tmpResult.getEndAmount() > bestResult.getEndAmount()) {
                bestResult = tmpResult;
            }
        }

        return bestResult;
    }

    private void analyze(HashMap<String, Node> nodes, HashMap<String, Node> previous, HashMap<String, Double> cost) {
        for (int i = 2; i < nodes.size(); i++) {
            boolean nothingChanged = true;
            for (Node node : nodes.values()) {
                for (NodeBridge bridge : node) {
                    if (cost.get(bridge.getEnd().getSymbol()) < bridge.getCost(cost.get(bridge.getStart().getSymbol()))) {
                        nothingChanged = false;
                        cost.replace(bridge.getEnd().getSymbol(), bridge.getCost(cost.get(bridge.getStart().getSymbol())));
                        previous.replace(bridge.getEnd().getSymbol(), node);
                    }
                }
            }
            if (nothingChanged) {
                break;
            }
        }
    }

    private Result correctResult(Result result, HashMap<String, Node> nodes, Configuration config) {
        if (result == null) {
            return null;
        }

        double amount = config.getAmount();
        String[] path = result.getPath();
        for (int i = 0; i < path.length - 1; i++) {
            NodeBridge bridge = nodes.get(path[i]).getBridgeTo(nodes.get(path[i + 1]));
            amount = bridge.getCost(amount);
        }

        result.setEndAmount(amount);

        return result;
    }

    private Result convertHashMapsIntoResult(HashMap<String, Node> nodes, HashMap<String, Node> previous, HashMap<String, Double> cost, Stack<HashMap<String, Node>> nodesSet, Configuration config) throws LoopException {
        if (nodes == null || previous == null || cost == null || nodesSet == null) {
            throw new IllegalArgumentException("Recived pointer at null.");
        }

        boolean doNotRemoveLoops = false;
        if (config.getFromCurrency().compareTo(config.getToCurrency()) == 0) {
            doNotRemoveLoops = true;
        }

        Result result = new Result();
        Stack<String> pathBackwards = new Stack<>();
        ArrayList<String> alreadyUsedCurrency = new ArrayList<>();

        /* The path is read backwards. Putting it on a stack to pop it off in the correct order later. */
        pathBackwards.push(config.getToCurrency());
        try {
            int maxRepeats = 0;
            maxRepeats = nodes.values().stream().map((node) -> node.getBridges().size()).reduce(maxRepeats, Integer::sum);

            if (previous.get(config.getToCurrency()) == null) {
                return null;
            }
            String previousNode = previous.get(config.getToCurrency()).getSymbol();

            while (previousNode.compareTo(config.getFromCurrency()) != 0) {
                if (alreadyUsedCurrency.contains(previousNode) && !doNotRemoveLoops) {
                    pathBackwards.push(previousNode);
                    breakLoop(nodes, pathBackwards, nodesSet);
                    throw new LoopException("A loop has been detected and fixed. Run analysis again.");
                }

                pathBackwards.push(previousNode);
                alreadyUsedCurrency.add(previousNode);

                previousNode = previous.get(previousNode).getSymbol();

                /* Protection from a loop in the HashMap */
                if (maxRepeats-- < 0) {
                    return null;
                }
            }
            pathBackwards.push(config.getFromCurrency());
        } catch (NullPointerException e) {
            return null;
        }

        String[] path = new String[pathBackwards.size()];
        for (int i = 0; i < path.length; i++) {
            path[i] = pathBackwards.pop();
        }

        result.setPath(path);
        result.setStartAmount(config.getAmount());
        result.setEndAmount(cost.get(config.getToCurrency()));

        return result;
    }

    private void breakLoop(HashMap<String, Node> nodes, Stack<String> pathBackwards, Stack<HashMap<String, Node>> nodesSet) {
        if (nodes == null || nodesSet == null) {
            throw new NullPointerException("Recived pointer at null.");
        }
        if (pathBackwards.isEmpty()) {
            throw new IllegalArgumentException("Recived stack is empty.");
        }

        HashMap<String, Node> tmpNodes = cloneMapAndNodes(nodes);

        String toCurr = pathBackwards.pop();
        String fromCurr = pathBackwards.pop();

        Node node = tmpNodes.get(toCurr);
        node.removeBridgeTo(tmpNodes.get(fromCurr));

        nodesSet.push(tmpNodes);
    }

    private HashMap<String, Node> cloneMapAndNodes(HashMap<String, Node> toCopy) {
        HashMap<String, Node> newMap = new HashMap<>();

        for (Node node : toCopy.values()) {
            newMap.put(node.getSymbol(), new Node(node));
        }

        return newMap;
    }

    private HashMap<String, Node> getMapOfPrevious(HashMap<String, Node> nodes) {
        if (nodes == null) {
            throw new IllegalArgumentException("Recived pointer at null");
        }

        HashMap<String, Node> previous = new HashMap<>();

        for (Node node : nodes.values()) {
            previous.put(node.getSymbol(), null);
        }

        return previous;
    }

    private HashMap<String, Double> getMapOfCost(Configuration config, HashMap<String, Node> nodes) {
        if (nodes == null) {
            throw new IllegalArgumentException("Recived pointer at null");
        }

        HashMap<String, Double> cost = new HashMap<>();

        for (Node node : nodes.values()) {
            cost.put(node.getSymbol(), -Double.MAX_VALUE);
        }

        cost.replace(config.getFromCurrency(), config.getAmount());

        return cost;
    }

}
