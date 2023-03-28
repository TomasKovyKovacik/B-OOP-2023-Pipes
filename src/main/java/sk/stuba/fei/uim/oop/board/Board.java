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
    private List<Node> path;
    private List<Direction> correctPath;

    public Board(int dimension) {
        this.random = new Random();
        this.path = new ArrayList<>();
        this.correctPath = new ArrayList<>();
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
        this.generatePath();
        this.createPath();

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.add(board[i][j]);
            }
        }
    }

    private void createPath() {
        for (int i = this.path.size() - 1; i >= 0 ; i--) {
            Node current = this.path.get(i);
            Node next = i - 1 < 0 ? null : this.path.get(i - 1);
            Node previous = i + 1 > this.path.size() - 1 ? null : this.path.get(i + 1);

            if (next == null && current.equals(this.end)) {
                Direction direction = this.checkDirection(current, previous);
                this.correctPath.add(0, direction);
                this.board[current.getY()][current.getX()] = new StartEnd(direction, false);
            } else if (previous == null && current.equals(this.start)) {
                Direction direction = this.checkDirection(current, next);
                this.correctPath.add(0, direction);
                this.board[current.getY()][current.getX()] = new StartEnd(direction, true);
            } else {
                this.checkPipes(current, next, previous);
            }
            this.board[current.getY()][current.getX()].randomRotate();
        }
    }

    private void checkPipes(Node current, Node next, Node previous) {
        Direction nextDirection = this.checkDirection(current, next);
        Direction prevDirection = this.checkDirection(current, previous);
        if ((nextDirection.equals(Direction.UP) && prevDirection.equals(Direction.DOWN)) ||
                (nextDirection.equals(Direction.DOWN) && prevDirection.equals(Direction.UP))) {
            this.correctPath.add(0, Direction.UP);
            this.board[current.getY()][current.getX()] = new StraightPipe(Direction.UP);
        } else if ((nextDirection.equals(Direction.LEFT) && prevDirection.equals(Direction.RIGHT)) ||
                (nextDirection.equals(Direction.RIGHT) && prevDirection.equals(Direction.LEFT))) {
            this.correctPath.add(0, Direction.LEFT);
            this.board[current.getY()][current.getX()] = new StraightPipe(Direction.LEFT);
        } else if ((nextDirection.equals(Direction.LEFT) && prevDirection.equals(Direction.DOWN)) ||
                (nextDirection.equals(Direction.DOWN) && prevDirection.equals(Direction.LEFT))) {
            this.correctPath.add(0, Direction.LEFT);
            this.board[current.getY()][current.getX()] = new BentPipe(Direction.LEFT);
        } else if ((nextDirection.equals(Direction.UP) && prevDirection.equals(Direction.LEFT)) ||
                (nextDirection.equals(Direction.LEFT) && prevDirection.equals(Direction.UP))) {
            this.correctPath.add(0, Direction.UP);
            this.board[current.getY()][current.getX()] = new BentPipe(Direction.UP);
        } else if ((nextDirection.equals(Direction.UP) && prevDirection.equals(Direction.RIGHT)) ||
                (nextDirection.equals(Direction.RIGHT) && prevDirection.equals(Direction.UP))) {
            this.correctPath.add(0, Direction.RIGHT);
            this.board[current.getY()][current.getX()] = new BentPipe(Direction.RIGHT);
        } else if ((nextDirection.equals(Direction.RIGHT) && prevDirection.equals(Direction.DOWN)) ||
                (nextDirection.equals(Direction.DOWN) && prevDirection.equals(Direction.RIGHT))) {
            this.correctPath.add(0, Direction.DOWN);
            this.board[current.getY()][current.getX()] = new BentPipe(Direction.DOWN);
        }
    }

    private void generatePath() {
        List<Node> visitedNodes = new ArrayList<>();
        Node currentPoint = new Node(this.start.getX(), this.start.getY());

        this.randomizedDFS(currentPoint, visitedNodes, this.path);
    }

    private Direction checkDirection(Node current, Node another) {
        int checkNextX = current.getX() - another.getX();
        int checkNextY = current.getY() - another.getY();
        if (checkNextX == 0 && checkNextY == 1) {
            return Direction.UP;
        } else if (checkNextX == 0 && checkNextY == -1){
            return Direction.DOWN;
        } else if (checkNextX == -1 && checkNextY == 0){
            return Direction.RIGHT;
        } else {
            return Direction.LEFT;
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

    public boolean checkCorectness() {
        for (int i = this.path.size() - 1; i >= 0 ; i--) {
            Node current = this.path.get(i);
            Direction correct = this.correctPath.get(i);
            Tile tile = this.board[current.getY()][current.getX()];
            if (tile instanceof StraightPipe) {
                if ((correct.equals(Direction.LEFT) || correct.equals(Direction.RIGHT)) && (tile.getDirection().equals(Direction.LEFT) || tile.getDirection().equals(Direction.RIGHT))) {
                    tile.setWater(true);
                } else if ((correct.equals(Direction.UP) || correct.equals(Direction.DOWN)) && (tile.getDirection().equals(Direction.UP) || tile.getDirection().equals(Direction.DOWN))) {
                    tile.setWater(true);
                } else {
                    return false;
                }
            } else {
                if (correct.equals(tile.getDirection())) {
                    tile.setWater(true);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

}
