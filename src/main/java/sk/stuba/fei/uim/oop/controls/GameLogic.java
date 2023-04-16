package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.Board;
import sk.stuba.fei.uim.oop.gui.Game;
import sk.stuba.fei.uim.oop.tile.Tile;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameLogic extends UniversalAdapter {

    public static final int INITIAL_BOARD_SIZE = 8;
    private Game mainGame;
    private Board currentBoard;
    private int currentBoardSize;
    @Getter
    private JLabel infoLabel;
    private int levelCounter;

    public GameLogic(Game mainGame) {
        this.mainGame = mainGame;
        this.currentBoardSize = INITIAL_BOARD_SIZE;
        this.initializeNewBoard(this.currentBoardSize);
        this.mainGame.add(this.currentBoard);
        this.infoLabel = new JLabel();
        this.levelCounter = 1;
        this.updateInfoLabel();
    }

    private void updateInfoLabel() {
        this.infoLabel.setText("LEVEL: " + levelCounter + ", BOARDSIZE: " + this.currentBoardSize);
        this.mainGame.revalidate();
        this.mainGame.repaint();
    }

    private void gameRestart(boolean reset) {
        this.mainGame.remove(this.currentBoard);
        this.initializeNewBoard(this.currentBoardSize);
        this.mainGame.add(this.currentBoard);
        if (reset) {
            this.levelCounter = 1;
        } else {
            this.levelCounter++;
        }
        this.updateInfoLabel();
        this.mainGame.setFocusable(true);
        this.mainGame.requestFocus();
    }

    private void initializeNewBoard(int dimension) {
        this.currentBoard = new Board(dimension);
        this.currentBoard.addMouseMotionListener(this);
        this.currentBoard.addMouseListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        this.currentBoardSize = ((JSlider) e.getSource()).getValue();
        this.gameRestart(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(Game.RESTART)) {
            this.gameRestart(true);
        } else {
            this.checkCorectness();
        }
        this.mainGame.revalidate();
        this.mainGame.repaint();
    }

    private void checkCorectness() {
        if (this.currentBoard.checkCorectness()) {
            this.gameRestart(false);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Component current = this.currentBoard.getComponentAt(e.getX(), e.getY());
        if (!(current instanceof Tile)) {
            return;
        }
        ((Tile) current).setHighlight(true);
        this.currentBoard.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Component current = this.currentBoard.getComponentAt(e.getX(), e.getY());
        if (!(current instanceof Tile)) {
            return;
        }
        ((Tile) current).rotate();
        this.currentBoard.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_R:
                this.gameRestart(true);
                break;
            case KeyEvent.VK_ESCAPE:
                this.mainGame.dispose();
            case KeyEvent.VK_ENTER:
                this.checkCorectness();
        }
    }
}
