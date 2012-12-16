package crapplet;

// CyberthelloPane.java

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

import checkers.Checker;
import checkers.CheckerGame;

public class CheckerPaneApplet extends JPanel {
    private static final Color bgColor = new Color(	128, 0, 0);

    private CheckerGameApplet game;

    private CheckerFrameApplet parentFrame;

    private int usableHeight;

    private int usableWidth;

    private int boardLeftOffset;

    private int boardTopOffset;

    private int squareHeight;

    private int squareWidth;

    public CheckerPaneApplet(CheckerFrameApplet cf) {
        super();
        game = null;
        setBackground(bgColor);
        parentFrame = cf;
        addMouseListener(new CheckerPaneMouseListener());
        //setOpaque(false);
        repaint();
    }

    public void setGame(CheckerGameApplet game) {
        this.game = game;
        repaint();
    }

    private void recalcSizes() {
        Dimension d = getSize();

        usableHeight = d.height - (d.height % 8) - 16;
        usableWidth = d.width - (d.width % 8) - 16;

        boardLeftOffset = 8;
        boardTopOffset = 8;

        squareHeight = usableHeight / 8;
        squareWidth = usableWidth / 8;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
     
        if (game == null)
            ;
        else {
            recalcSizes();
            g2.setPaint(Color.black);
            for (int i = 0; i <= 8; ++i) {
                g2.draw(new Line2D.Double(boardLeftOffset, boardTopOffset
                        + (i * squareHeight), boardLeftOffset
                        + (8 * squareWidth), boardTopOffset
                        + (i * squareHeight)));
                g2.draw(new Line2D.Double(boardLeftOffset + (i * squareWidth),
                        boardTopOffset, boardLeftOffset + (i * squareWidth),
                        boardTopOffset + (8 * squareHeight)));
            }
            int ellipseWidth = (squareWidth / 5) * 4;
            int ellipseHeight = (squareHeight / 5) * 4;
            int ellipseWidthOffset = (squareWidth - ellipseWidth) / 2;
            int ellipseHeightOffset = (squareHeight - ellipseHeight) / 2;

          
            int count = 0;
            for (int row = 0; row < 8; ++row)
                for (int col = 0; col < 8; ++col) {
                    int squareContents = game.getSquareContents(row, col);
                   
                    if(((++count%9==0)?++count:count)%2==0) {
                        g2.setPaint(Color.black);
                        g2.fill(new Rectangle2D.Double(boardLeftOffset + (col* squareWidth),
                                boardTopOffset + (row * squareHeight), squareWidth,squareHeight));
                    }
                    
                    if (squareContents != Checker.EMPTY) {
                        int small = (squareContents == Checker.BLACK)?1:2;
                        g2.setPaint((squareContents == Checker.BLACK)?Color.WHITE:Color.DARK_GRAY);
                        g2.fill(new Ellipse2D.Double(boardLeftOffset
                                + (col * squareWidth) + ellipseWidthOffset+ small,
                                boardTopOffset + (row * squareHeight)
                                        +ellipseHeightOffset+ small, ellipseWidth,
                                ellipseHeight));
                        if (squareContents == Checker.BLACK) {
                            g2.setPaint(Color.BLUE);
                        }
                        if (squareContents == Checker.BKing) {
                            g2.setPaint(Color.GREEN);
                        }

                        if (squareContents == Checker.WHITE) {
                            g2.setPaint(Color.white);
                        }
                        if (squareContents == Checker.WKing) {
                            g2.setPaint(Color.yellow);
                        }

                        g2.fill(new Ellipse2D.Double(boardLeftOffset
                                + (col * squareWidth) + ellipseWidthOffset,
                                boardTopOffset + (row * squareHeight)
                                        + ellipseHeightOffset, ellipseWidth,
                                ellipseHeight));
                        
                    }
                }
            int sr = game.lastMove[0];
            int sc = game.lastMove[1];
            int cr = game.lastMove[2];
            int cc = game.lastMove[3];
            if(sr != -1) {
	            g2.setColor(Color.GREEN);
	            g2.drawRect( boardLeftOffset + (sc* squareWidth),
	                    boardTopOffset + (sr * squareHeight),squareWidth, squareHeight);
	            g2.drawRect( boardLeftOffset + (cc* squareWidth),
	                    boardTopOffset + (cr * squareHeight),squareWidth, squareHeight);
            }
        }
    }

    private int xCoordToCol(int xCoord) {
        if (xCoord < boardLeftOffset) {
            return -1;
        }

        xCoord -= boardLeftOffset;

        if (xCoord > squareWidth * 8) {
            return -1;
        }
        return xCoord / squareWidth;
    }

    private int yCoordToRow(int yCoord) {
        if (yCoord < boardTopOffset) {
            return -1;
        }

        yCoord -= boardTopOffset;

        if (yCoord > squareHeight * 8) {
            return -1;
        }
        return yCoord / squareHeight;
    }

    // inner class
    public class CheckerPaneMouseListener extends MouseAdapter {
        private int pressedCol, pressedRow;

        public void mousePressed(MouseEvent e) {
            recalcSizes();
            pressedCol = xCoordToCol(e.getX());
            pressedRow = yCoordToRow(e.getY());
        }

        public void mouseReleased(MouseEvent e) {
            int releasedX = e.getX();
            int releasedY = e.getY();

            recalcSizes();

            int releasedCol = xCoordToCol(releasedX);
            int releasedRow = yCoordToRow(releasedY);

            if (pressedCol != releasedCol && pressedRow != releasedRow) {
                if (releasedCol != -1 && releasedRow != -1) {
                    parentFrame.makeMove(pressedRow, pressedCol, releasedRow,
                            releasedCol);
                    repaint();
                }
            }
            pressedRow = pressedCol = -1; // set to invalid
        }
    }
}

