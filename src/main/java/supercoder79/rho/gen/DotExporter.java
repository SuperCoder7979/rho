package supercoder79.rho.gen;

import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import supercoder79.rho.ast.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public final class DotExporter {
    public static void toDotFile(Node node, String name) {
        StringBuilder builder = new StringBuilder();

        builder.append("digraph ").append(name).append(" {\n");
        int id = 0;
        Reference2IntOpenHashMap<Node> ids = new Reference2IntOpenHashMap<>();

        Deque<Node> deque = new LinkedList<>();
        deque.add(node);

        while (!deque.isEmpty()) {
            Node nd = deque.removeFirst();
            ids.put(nd, ++id);

            deque.addAll(nd.children());
        }

        for (Map.Entry<Node, Integer> e : ids.entrySet()) {
            Integer val = e.getValue();
            Node nd = e.getKey();

            builder.append("\t").append(val).append(" [label=\"").append(nd.getClass().getSimpleName());
            String dotLabel = nd.getDotNodeLabel();
            if (!dotLabel.isEmpty()) {
                builder.append("\n");
                builder.append(dotLabel);
            }

            builder.append("\"];\n");

            for (Node c : nd.children()) {
                builder.append("\t").append(val).append(" -> ").append(ids.get(c));

                String edgeLabel = nd.getDotEdgeLabel(c);
                if (!edgeLabel.isEmpty()) {
                    builder.append(" [label=\"");
                    builder.append(edgeLabel);
                    builder.append("\"]");
                }

                builder.append(";\n");
            }
        }

        builder.append("}");

        try {
            Files.writeString(Path.of(name + ".dot"), builder.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
