package sk.stuba.fei.uim.oop.tile;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.board.Direction;
import sk.stuba.fei.uim.oop.board.Node;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Tile extends JPanel {

    @Getter @Setter
    protected Direction direction;
    @Setter
    protected boolean highlight;
    @Setter
    protected boolean water;
    private Random random;

    public Tile(Direction direction) {
        this();
        this.direction = direction;
    }

    public Tile() {
        this.random = new Random();
        this.direction = Direction.UP;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.highlight) {
            g.setColor(Color.RED);
            ((Graphics2D) g).setStroke(new BasicStroke(3));
            g.drawRect(2, 2,
                    this.getWidth() - 4,this.getHeight() - 4);
            this.highlight = false;
        }
    }

    protected void resolveWaterColor(Graphics g) {
        if (this.water) {
            g.setColor(Color.CYAN);
            this.water = false;
        } else {
            g.setColor(Color.GRAY);
        }
    }

    public void rotate() {
        this.direction = this.direction.rotate();
    }

    public void randomRotate() {
        for (int i = 0; i < this.random.nextInt(3); i++) {
            this.rotate();
        }
    }

    public Direction getOtherDirection() {
        return null;
    }
}
