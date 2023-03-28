package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Node {
    @Getter @Setter
    private List<Node> neighbours;
    @Getter @Setter
    private int x;
    @Getter @Setter
    private int y;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.neighbours = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            return ((Node) obj).getX() == this.getX() && ((Node) obj).getY() == this.getY();
        }
        return false;
    }
}
