package sk.stuba.fei.uim.oop.tile;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public class Tile extends JPanel {

    @Getter @Setter
    protected Rotation rotation;
    @Setter
    protected boolean highlight;

    public Tile(Rotation rotation) {
        this.rotation = rotation;
    }

    public Tile() {
        this.rotation = Rotation.LEFT;
    }

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
}
