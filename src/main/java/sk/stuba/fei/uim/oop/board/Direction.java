package sk.stuba.fei.uim.oop.board;

import lombok.Getter;

public enum Direction {
    RIGHT(1, 0),
    UP(0, 1),
    LEFT(-1, 0),
    DOWN(0, -1);

    @Getter
    private int x;
    @Getter
    private int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Direction rotate() {
        if (this.equals(Direction.RIGHT)) {
            return Direction.DOWN;
        } else if (this.equals(Direction.DOWN)) {
            return Direction.LEFT;
        } else if (this.equals(Direction.LEFT)) {
            return Direction.UP;
        } else  {
            return Direction.RIGHT;
        }
    }
}
