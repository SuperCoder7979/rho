package supercoder79.rho.ast.high;

import supercoder79.rho.ast.Node;
import supercoder79.rho.ast.low.InsnNode;
import supercoder79.rho.gen.CodegenContext;

import java.util.List;

public record MaxNode(Node left, Node right) implements Node {
    @Override
    public Node lower(CodegenContext ctx) {
        return new InsnNode(INVOKESTATIC, "java/lang/Math", "max", "(D)D", left.lower(ctx), right.lower(ctx));
    }

    @Override
    public Node replaceNode(Node old, Node newNode) {
        if (old == left) {
            return new MaxNode(newNode, right);
        } else if (old == right) {
            return new MaxNode(left, newNode);
        }

        return this;
    }

    @Override
    public List<Node> children() {
        return List.of(left, right);
    }
}
