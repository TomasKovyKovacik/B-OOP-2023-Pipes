package sk.stuba.fei.uim.oop.board;

import sk.stuba.fei.uim.oop.tile.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board extends JPanel {
    private Tile[][] board;
    private Random random;
    private Node start;
    private Node end;

    public Board(int dimension) {
        this.random = new Random();
        this.initializeBoard(dimension);
        this.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        this.setBackground(Color.CYAN);
    }

    private void initializeBoard(int dimension) {
        this.board = new Tile[dimension][dimension];
        this.setLayout(new GridLayout(dimension, dimension));

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.board[i][j] = new Tile();
            }
        }

        this.start = new Node(0, this.random.nextInt(dimension - 1));
        this.end = new Node(dimension - 1, this.random.nextInt(dimension - 1));
        this.createPath(this.generatePath());
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.add(board[i][j]);
            }
        }
    }

    private void createPath(List<Node> path) {
        for (int i = path.size() - 1; i >= 0 ; i--) {
            Node current = path.get(i);
            Node next = i - 1 < 0 ? null : path.get(i - 1);
            Node previous = i + 1 > path.size() - 1 ? null : path.get(i + 1);

            if (next == null && current.equals(this.end)) {
                Rotation rotation = this.checkRotation(current, previous);
                this.board[current.getY()][current.getX()] = new StartEnd(rotation);
            } else if (previous == null && current.equals(this.start)) {
                Rotation rotation = this.checkRotation(current, next);
                this.board[current.getY()][current.getX()] = new StartEnd(rotation);
            } else {
                Rotation nextRotation = this.checkRotation(current, next);
                Rotation prevRotation = this.checkRotation(current, previous);
                if ((nextRotation.equals(Rotation.UP) && prevRotation.equals(Rotation.DOWN)) ||
                        (nextRotation.equals(Rotation.DOWN) && prevRotation.equals(Rotation.UP))) {
                    this.board[current.getY()][current.getX()] = new StraightPipe(Rotation.UP);
                } else if ((nextRotation.equals(Rotation.LEFT) && prevRotation.equals(Rotation.RIGHT)) ||
                        (nextRotation.equals(Rotation.RIGHT) && prevRotation.equals(Rotation.LEFT))) {
                    this.board[current.getY()][current.getX()] = new StraightPipe(Rotation.LEFT);
                } else {
                    this.board[current.getY()][current.getX()] = new BentPipe(Rotation.UP);
                }

            }
        }
    }

    private List<Node> generatePath() {
        List<Node> path = new ArrayList<>();
        List<Node> visitedNodes = new ArrayList<>();
        Node currentPoint = new Node(this.start.getX(), this.start.getY());

        this.randomizedDFS(currentPoint, visitedNodes, path);

        return path;
    }

    private Rotation checkRotation(Node current, Node another) {
        int checkNextX = current.getX() - another.getX();
        int checkNextY = current.getY() - another.getY();
        if (checkNextX == 0 && checkNextY == 1) {
            return Rotation.UP;
        } else if (checkNextX == 0 && checkNextY == -1){
            return Rotation.DOWN;
        } else if (checkNextX == -1 && checkNextY == 0){
            return Rotation.RIGHT;
        } else {
            return Rotation.LEFT;
        }
    }

    private boolean randomizedDFS(Node node, List<Node> visitedNodes, List<Node> path) {
        visitedNodes.add(node);
        this.setNeighbours(node);
        Node nextPoint = this.getUnvisitedNeighbour(node, visitedNodes);
        while (nextPoint != null) {
            if (nextPoint.equals(this.end)) {
                path.add(nextPoint);
                path.add(node);
                return true;
            }
            boolean findEnd = randomizedDFS(nextPoint, visitedNodes, path);
            if (findEnd) {
                path.add(node);
                return true;
            }
            nextPoint = this.getUnvisitedNeighbour(node, visitedNodes);
        }
        return false;
    }

    private Node getUnvisitedNeighbour(Node node, List<Node> visitedNodes) {
        List<Node> unvisited = new ArrayList<>();
        for (Node n : node.getNeighbours()) {
            if (containsNode(visitedNodes, n) == null) {
                unvisited.add(n);
            }
        }
        return unvisited.size() == 0 ? null : unvisited.get(this.random.nextInt(unvisited.size()));
    }

    private Node containsNode(List<Node> path, Node node) {
        for (Node n : path) {
            if (n.equals(node)) {
                return n;
            }
        }
        return null;
    }

    private void setNeighbours(Node node) {
        List<Node> neighbours = new ArrayList<>();
        for (Direction d : Direction.values()) {
            int checkX = node.getX() + d.getX();
            int checkY = node.getY() + d.getY();
            if (checkX >= 0 && checkY >= 0 && checkX < this.board.length && checkY < this.board.length) {
                neighbours.add(new Node(checkX, checkY));
            }
        }
        node.setNeighbours(neighbours);
    }
}
