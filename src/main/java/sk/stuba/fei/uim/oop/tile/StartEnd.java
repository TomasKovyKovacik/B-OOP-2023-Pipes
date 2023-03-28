package sk.stuba.fei.uim.oop.tile;

import sk.stuba.fei.uim.oop.board.Direction;

import java.awt.*;

public class StartEnd extends Tile {
    private boolean start;
    public StartEnd(Direction direction, boolean start) {
        super(direction);
        this.start = start;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (start) {
            g.setColor(Color.BLUE);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
        g.fillOval((int) (0 + this.getWidth() * 0.15), (int) (0 + this.getHeight() * 0.15),
                (int) (this.getWidth() * 0.7), (int) (this.getHeight() * 0.7));
        if (this.direction.equals(Direction.UP)) {
            g.fillRect((int) (this.getWidth() * 0.3), 0, (int) (this.getWidth() * 0.4), this.getHeight() / 2);
        } else if (this.direction.equals(Direction.DOWN)) {
            g.fillRect((int) (this.getWidth() * 0.3), this.getHeight() / 2, (int) (this.getWidth() * 0.4) , this.getHeight() / 2);
        } else if (this.direction.equals(Direction.LEFT)) {
            g.fillRect(0, (int) (this.getHeight() * 0.3), this.getWidth() / 2 , (int) (this.getHeight() * 0.4));
        } else if (this.direction.equals(Direction.RIGHT)) {
            g.fillRect(this.getWidth() / 2, (int) (this.getHeight() * 0.3), this.getWidth() / 2 , (int) (this.getHeight() * 0.4));
        }
    }
}
