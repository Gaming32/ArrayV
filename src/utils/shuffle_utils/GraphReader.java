package utils.shuffle_utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import utils.Distributions;
import utils.ShuffleGraph;
import utils.ShuffleInfo;
import utils.Shuffles;

public final class GraphReader {
    public final class MalformedGraphFileException extends Exception {
        // public int line, column;

        public MalformedGraphFileException() {
            super();
            // this.line = 0;
            // this.column = 0;
        }

        // public MalformedGraphFileException(int line, int column) {
        //     super();
        //     this.line = line;
        //     this.column = column;
        // }

        public MalformedGraphFileException(String message) {
            super(message);
            // this.line = 0;
            // this.column = 0;
        }

        // public MalformedGraphFileException(String message, int line, int column) {
        //     super(message);
        //     this.line = line;
        //     this.column = column;
        // }
    }
    
    private final class PartialElement {
        int left, right;

        public PartialElement(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }

    Scanner scanner;
    ShuffleGraph result;
    List<PartialElement> partialNodes;

    public GraphReader() {
        result = null;
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
        result = new ShuffleGraph();
        partialNodes = new ArrayList<>();

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
            Node node = result.nodes.get(i);
            PartialElement partial = partialNodes.get(i - 1);
            try {
                node.preConnection = partial.left == -1 ? null : result.connections.get(partial.left);
                node.postConnection = partial.right == -1 ? null : result.connections.get(partial.right);
            } catch (IndexOutOfBoundsException e) {
                String message = e.getMessage();
                int id = Integer.parseInt(message.split(" ", 3)[1]);
                MalformedGraphFileException newError = new MalformedGraphFileException("No connection with the ID " + id);
                newError.initCause(e);
                throw newError;
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
                Distributions distribution = Distributions.valueOf(name);
                shuffleInfo = new ShuffleInfo(distribution);
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
        if (!scanner.hasNextInt()) {
            throw new MalformedGraphFileException("Expected X coordinate in node declaration");
        }
        int x = scanner.nextInt();
        if (!scanner.hasNextInt()) {
            throw new MalformedGraphFileException("Expected Y coordinate in node declaration");
        }
        int y = scanner.nextInt();
        if (!scanner.hasNextInt()) {
            throw new MalformedGraphFileException("Expected preConnection ID in node declaration");
        }
        int preConnectionID = scanner.nextInt();
        if (!scanner.hasNextInt()) {
            throw new MalformedGraphFileException("Expected postConnection ID in node declaration");
        }
        int postConnectionID = scanner.nextInt();

        result.nodes.add(new Node(shuffleInfo, result, x, y));
        partialNodes.add(new PartialElement(preConnectionID, postConnectionID));
    }

    private void readConnection() throws MalformedGraphFileException {
        if (!scanner.hasNextInt()) {
            throw new MalformedGraphFileException("Expected fromNode ID in connection declaration");
        }
        int fromNodeID = scanner.nextInt();
        if (!scanner.hasNextInt()) {
            throw new MalformedGraphFileException("Expected toNode ID in connection declaration");
        }
        int toNodeID = scanner.nextInt();

        Node fromNode = null, toNode = null;
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
        Connection connection = new Connection(fromNode, toNode);
        result.connections.add(connection);
        if (fromNodeID == 0) {
            fromNode.postConnection = connection;
        }
    }
}
