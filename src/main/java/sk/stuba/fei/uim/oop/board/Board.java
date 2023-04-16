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

    public Board(int dimension) {
        this.random = new Random();
        this.path = new ArrayList<>();
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
                this.board[current.getY()][current.getX()] = new StartEnd(direction, false);
            } else if (previous == null && current.equals(this.start)) {
                Direction direction = this.checkDirection(current, next);
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
        if (this.oppositeDirections(nextDirection, prevDirection)) {
            this.board[current.getY()][current.getX()] = new StraightPipe(Direction.UP);
        } else {
            this.board[current.getY()][current.getX()] = new BentPipe(Direction.UP);
        }
    }

    private void generatePath() {
        List<Node> visitedNodes = new ArrayList<>();
        Node currentPoint = new Node(this.start.getX(), this.start.getY());

        this.randomizedDFS(currentPoint, visitedNodes, this.path);
    }

    private Direction checkDirection(Node current, Node another) {
        int checkNextX = another.getX() - current.getX();
        int checkNextY = another.getY() - current.getY();
        if (checkNextX == 0 && checkNextY == 1) {
            return Direction.DOWN;
        } else if (checkNextX == 0 && checkNextY == -1){
            return Direction.UP;
        } else if (checkNextX == -1 && checkNextY == 0){
            return Direction.LEFT;
        } else {
            return Direction.RIGHT;
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
        Node current = this.start;
        Direction currentDirection = this.board[current.getY()][current.getX()].getDirection();
        while (true) {
            int checkX = current.getX() + currentDirection.getX();
            int checkY = current.getY() + currentDirection.getY();
            if (checkX < 0 || checkY < 0 || checkX >= this.board.length || checkY >= this.board.length) {
                return false;
            }
            Node next = new Node(checkX, checkY);
            Tile tile = this.board[next.getY()][next.getX()];
            if (!(tile instanceof BentPipe) &&
                    !(tile instanceof StartEnd) &&
                    !(tile instanceof StraightPipe)) {
                return false;
            }
            if (tile instanceof StartEnd) {
                return this.oppositeDirections(currentDirection, tile.getDirection());
            }
            Direction direction1 = tile.getDirection();
            Direction direction2 = tile.getOtherDirection();
            if (this.oppositeDirections(currentDirection, direction1)) {
                tile.setWater(true);
                current = next;
                currentDirection = direction2;
                continue;
            }
            if (this.oppositeDirections(currentDirection, direction2)) {
                tile.setWater(true);
                current = next;
                currentDirection = direction1;
                continue;
            }
            return false;
        }
    }

    private boolean oppositeDirections(Direction direction1, Direction direction2) {
        return direction1.getX() + direction2.getX() == 0 && direction1.getY() + direction2.getY() == 0;
    }

}
