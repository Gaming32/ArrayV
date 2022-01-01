package utils.shuffleutils;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import utils.Distributions;
import utils.ShuffleGraph;
import utils.ShuffleInfo;
import utils.Shuffles;

public final class GraphReader {
    public final class MalformedGraphFileException extends Exception {
        public MalformedGraphFileException() {
            super();
        }

        public MalformedGraphFileException(String message) {
            super(message);
        }
    }

    private final class PartialElement {
        int left, right;

        public PartialElement(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }

    public final static int[] COMPATIBLE_VERSIONS = {0, 1, 2, 3};
    static Set<Integer> compatibleVersionsSet;

    Scanner scanner;
    ShuffleGraph result;
    List<PartialElement> partialNodes;
    int version;

    public GraphReader() {
        result = null;
        if (compatibleVersionsSet == null) {
            compatibleVersionsSet = Collections.unmodifiableSet(
                Arrays.stream(COMPATIBLE_VERSIONS)
                      .boxed()
                      .collect(Collectors.toSet())
            );
        }
    }

    public ShuffleGraph getResult() {
        return result;
    }

    public ShuffleGraph read(String fileName) throws IOException, MalformedGraphFileException {
        scanner = new Scanner(fileName);
        try {
            read();
        } finally {
            scanner.close();
        }
        return result;
    }

    public ShuffleGraph read(File file) throws IOException, MalformedGraphFileException {
        scanner = new Scanner(file);
        try {
            read();
        } finally {
            scanner.close();
        }
        return result;
    }

    public ShuffleGraph read(Scanner scanner) throws IOException, MalformedGraphFileException {
        this.scanner = scanner;
        try {
            read();
        } finally {
            scanner.close();
        }
        return result;
    }

    private void read() throws IOException, MalformedGraphFileException {
        version = scanner.hasNextInt() ? scanner.nextInt() : 0;
        if (!compatibleVersionsSet.contains(version)) {
            throw new MalformedGraphFileException("Unsupported version for reading: " + version + " (Supported versions: "
                + Arrays.stream(COMPATIBLE_VERSIONS)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(", ", "{", "}")) + ")");
        }
        result = new ShuffleGraph();
        partialNodes = new ArrayList<>();

        if (version >= 2 && scanner.hasNextDouble()) {
            result.sleepRatio = scanner.nextDouble();
        }

        while (scanner.hasNext()) {
            String identifier = scanner.next().toUpperCase();
            switch (identifier) {
                case "N":
                    readNode();
                    break;
                case "C":
                    readConnection();
                    break;
                default:
                    throw new MalformedGraphFileException("Invalid element type \"" + identifier + "\"");
            }
        }

        for (int i = 1; i < result.nodes.size(); i++) {
            GraphNode node = result.nodes.get(i);
            PartialElement partial = partialNodes.get(i - 1);
            try {
                if (version < 3) {
                    node.preConnection = partial.left == -1 ? null : result.connections.get(partial.left);
                    node.postConnection = partial.right == -1 ? null : result.connections.get(partial.right);
                } else {
                    if (partial.left != -1 && node.preConnection == null) {
                        GraphNode from = result.nodes.get(partial.left);
                        GraphConnection newConnection = new GraphConnection(from, node);
                        result.connections.add(newConnection);
                        from.postConnection = newConnection;
                        node.preConnection = newConnection;
                    }
                    if (partial.right != -1 && node.postConnection == null) {
                        GraphNode to = result.nodes.get(partial.right);
                        GraphConnection newConnection = new GraphConnection(node, to);
                        result.connections.add(newConnection);
                        node.postConnection = newConnection;
                        to.preConnection = newConnection;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                String message = e.getMessage();
                int id = Integer.parseInt(message.split(" ", 3)[1]);
                MalformedGraphFileException newError = new MalformedGraphFileException("No connection with the ID " + id);
                newError.initCause(e);
                throw newError;
            }
        }

        if (version >= 2) {
            for (int i = 1; i < result.nodes.size(); i++) {
                GraphNode node = result.nodes.get(i);
                if (node.x == Integer.MIN_VALUE) { // coordinates not specified
                    if (node.preConnection == null || node.preConnection.from == null) {
                        Point safePos = result.findSafeCoordinates(100, 100, 20, 20);
                        node.x = safePos.x;
                        node.y = safePos.y;
                    } else {
                        GraphNode previous = node.preConnection.from;
                        node.x = previous.x + GraphNode.WIDTH + 15;
                        node.y = previous.y;
                    }
                }
            }
        }

        partialNodes = null;
    }

    private void readNode() throws MalformedGraphFileException {
        if (!scanner.hasNextBoolean()) {
            throw new MalformedGraphFileException("Expected isDistribution in node declaration");
        }
        boolean isDistribution = scanner.nextBoolean();
        if (!scanner.hasNext()) {
            throw new MalformedGraphFileException("Unexpected EOF during node parsing");
        }
        String name = scanner.next();
        ShuffleInfo shuffleInfo;
        try {
            if (isDistribution) {
                boolean isDistributionWarped = false;
                if (version > 0) {
                    if (!scanner.hasNextBoolean()) {
                        throw new MalformedGraphFileException("Expected isDistributionWarped in node declaration");
                    }
                    isDistributionWarped = scanner.nextBoolean();
                }
                Distributions distribution = Distributions.valueOf(name);
                shuffleInfo = new ShuffleInfo(distribution, isDistributionWarped);
            } else {
                Shuffles shuffle = Shuffles.valueOf(name);
                shuffleInfo = new ShuffleInfo(shuffle);
            }
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            if (message.startsWith("No enum constant utils.")) {
                message = message.substring("No enum constant utils.".length());
                if (message.startsWith("Shuffles.")) {
                    message = "No shuffle with the ID \"" + message.substring("Shuffles.".length()) + "\"";
                } else if (message.startsWith("Distributions.")) {
                    message = "No distribution with the ID \"" + message.substring("Distributions.".length()) + "\"";
                } else {
                    throw e;
                }
                MalformedGraphFileException newError = new MalformedGraphFileException(message);
                newError.initCause(e);
                throw newError;
            }
            throw e;
        }
        int x, y, preConnectionID, postConnectionID;
        if (!scanner.hasNextInt()) { // x
            throw new MalformedGraphFileException("Expected X coordinate in node declaration");
        }
        x = scanner.nextInt();
        if (!scanner.hasNextInt()) { // y
            throw new MalformedGraphFileException("Expected Y coordinate in node declaration");
        }
        y = scanner.nextInt();
        if (version < 2) {
            if (!scanner.hasNextInt()) { // preConnectionID
                throw new MalformedGraphFileException("Expected preConnection ID in node declaration");
            }
            preConnectionID = scanner.nextInt();
            if (!scanner.hasNextInt()) { // postConnectionID
                throw new MalformedGraphFileException("Expected postConnection ID in node declaration");
            }
            postConnectionID = scanner.nextInt();
        } else {
            if (scanner.hasNextInt()) {
                preConnectionID = scanner.nextInt();
                if (!scanner.hasNextInt()) { // postConnectionID
                    throw new MalformedGraphFileException("Expected postConnection ID in node declaration");
                }
                postConnectionID = scanner.nextInt();
            } else {
                preConnectionID = x;
                postConnectionID = y;
                x = Integer.MIN_VALUE;
            }
        }

        result.nodes.add(new GraphNode(shuffleInfo, result, x, y));
        partialNodes.add(new PartialElement(preConnectionID, postConnectionID));
    }

    private void readConnection() throws MalformedGraphFileException {
        if (version >= 3) {
            throw new MalformedGraphFileException("Invalid identifier type \"C\": Connections were removed in format v3");
        }
        if (!scanner.hasNextInt()) {
            throw new MalformedGraphFileException("Expected fromNode ID in connection declaration");
        }
        int fromNodeID = scanner.nextInt();
        if (!scanner.hasNextInt()) {
            throw new MalformedGraphFileException("Expected toNode ID in connection declaration");
        }
        int toNodeID = scanner.nextInt();

        GraphNode fromNode = null, toNode = null;
        try {
            fromNode = fromNodeID == -1 ? null : result.nodes.get(fromNodeID);
            toNode = toNodeID == -1 ? null : result.nodes.get(toNodeID);
        } catch (IndexOutOfBoundsException e) {
            String message = e.getMessage();
            int id = Integer.parseInt(message.split(" ", 3)[1]);
            MalformedGraphFileException newError = new MalformedGraphFileException("No node with the ID " + id);
            newError.initCause(e);
            throw newError;
        }
        GraphConnection connection = new GraphConnection(fromNode, toNode);
        result.connections.add(connection);
        if (fromNodeID == 0) {
            fromNode.postConnection = connection;
        }
    }
}
