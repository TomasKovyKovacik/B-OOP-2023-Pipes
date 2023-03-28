package sk.stuba.fei.uim.oop.tile;

import java.awt.*;

public class StraightPipe extends Tile{

    public StraightPipe(Rotation rotation) {
        super(rotation);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.getRotation().equals(Rotation.UP) || this.getRotation().equals(Rotation.DOWN)) {
            g.setColor(Color.GRAY);
            g.fillRect((int) (this.getWidth() * 0.3), 0 , (int) (this.getWidth() * 0.4), this.getHeight());
            g.setColor(Color.BLACK);
            ((Graphics2D) g).setStroke(new BasicStroke(3));
            g.drawLine((int) (this.getWidth() * 0.3), 0, (int) (this.getWidth() * 0.3), this.getHeight());
            g.drawLine((int) (this.getWidth() * 0.7), 0, (int) (this.getWidth() * 0.7), this.getHeight());
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(0, (int) (this.getHeight() * 0.3) , this.getWidth(), (int) (this.getHeight() * 0.4));
            g.setColor(Color.BLACK);
            ((Graphics2D) g).setStroke(new BasicStroke(3));
            g.drawLine(0,(int) (this.getHeight() * 0.3), this.getWidth(),(int) (this.getHeight() * 0.3));
            g.drawLine(0,(int) (this.getHeight() * 0.7), this.getWidth(),(int) (this.getHeight() * 0.7));
        }
    }
}
