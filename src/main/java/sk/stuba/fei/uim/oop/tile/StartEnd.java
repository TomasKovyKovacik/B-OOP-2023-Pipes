package sk.stuba.fei.uim.oop.tile;

import java.awt.*;

public class StartEnd extends Tile {

    public StartEnd(Rotation rotation) {
        super(rotation);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillOval((int) (0 + this.getWidth() * 0.15), (int) (0 + this.getHeight() * 0.15),
                (int) (this.getWidth() * 0.7), (int) (this.getHeight() * 0.7));
        if (this.rotation.equals(Rotation.UP)) {
            g.fillRect((int) (this.getWidth() * 0.3), 0, (int) (this.getWidth() * 0.4), this.getHeight() / 2);
        } else if (this.rotation.equals(Rotation.DOWN)) {
            g.fillRect((int) (this.getWidth() * 0.3), this.getHeight() / 2, (int) (this.getWidth() * 0.4) , this.getHeight() / 2);
        } else if (this.rotation.equals(Rotation.LEFT)) {
            g.fillRect(0, (int) (this.getHeight() * 0.3), this.getWidth() / 2 , (int) (this.getHeight() * 0.4));
        } else if (this.rotation.equals(Rotation.RIGHT)) {
            g.fillRect(this.getWidth() / 2, (int) (this.getHeight() * 0.3), this.getWidth() / 2 , (int) (this.getHeight() * 0.4));
        }
    }
}
