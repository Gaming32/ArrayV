package utils.shuffleutils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import utils.ShuffleGraph;

public final class GraphWriter {
    public static final int VERSION = 3;

    ShuffleGraph graph;

    public GraphWriter(ShuffleGraph graph) {
        this.graph = graph;
    }

    public void write(String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        write(writer);
    }

    public void write(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        write(writer);
    }

    public void write(FileWriter writer) throws IOException {
        Map<GraphNode, Integer> nodeMap = new HashMap<>();
        for (int i = 0; i < graph.nodes.size(); i++) {
            GraphNode node = graph.nodes.get(i);
            nodeMap.put(node, i);
        }
        nodeMap.put(null, -1);

        // Metadata
        writer.write(VERSION + " ");
        writer.write(graph.sleepRatio + "\n");

        // Nodes
        for (int i = 1; i < graph.nodes.size(); i++) {
            GraphNode node = graph.nodes.get(i);
            writer.write("N ");
            if (node.getValue().isDistribution()) {
                writer.write("true ");
                writer.write(node.getValue().getDistribution().name() + " ");
                writer.write(node.getValue().isDistributionWarped() + " ");
            } else {
                writer.write("false ");
                writer.write(node.getValue().getShuffle().name() + " ");
            }
            writer.write(node.x + " ");
            writer.write(node.y + " ");
            writer.write((node.preConnection == null ? -1 : nodeMap.get(node.preConnection.from)) + " ");
            writer.write((node.postConnection == null ? -1 : nodeMap.get(node.postConnection.to)) + "\n");
        }

        writer.close();
    }
}
