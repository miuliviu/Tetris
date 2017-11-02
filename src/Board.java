/**
 * Created by Liviu-Marius Miu on 02/12/2016.
 * Reg. No:1502419
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Board Class is where all the action is happening.
 * It extends JPanel and is included in the Tetris object (Main Game)
 * */
public class Board extends JPanel implements ActionListener {


    //Declaring a Timer which will temporise the whole session of the gameplay
    Timer timp;

    //Declaring a boolean for finished falling action
    boolean cadereTerminata = false;

    //Declaring a boolean for the start of the session
    boolean Pornit = false;

    //Counter for the deleted lines which count as score
    int liniiScoase = 0;

    //Declaring position coordinates
    int curX = 0;
    int curY = 0;
    int spaceuri=0;
    //Declaring a JLabel which will display the score in the JFrame
    JLabel score;

    //Declaring a shape for the current tetris shape and an array of shapes for the tetris grid
    Shape curForma;
    Shape.Piesa[] masa;

    //Declaring the Columns and the Rows of the Tetris Grid/Playground
    final int Coloane = 10;
    final int Linii = 20;

    Tetris joc;


    //Constructor of the Board Class Taking as parameter a tetris object which extend the JFrame Class
    public Board(Tetris joc) {

        this.joc = joc;

        //Making the Board Panel Focusable so the events coming from mouse and from other sources will be returned to this panel
        setFocusable(true);

        //Creating a new Tetris shape and saving it in a the variable curForma which is the current Tetris Shape
        curForma = new Shape();

        //Creating and starting a timer which will separate the movement of the current object and of the methods called by 500 ms
        timp = new Timer(400, this);
        timp.start();

        //Creating the Tetris grid made of 10*20 tetris NoShapes
        masa = new Shape.Piesa[Coloane * Linii];

        //Adding MouseListener and MouseWheelListener to the Board Panel to listen to actions and events triggered by the mouse
        addMouseListener(new mouseClick());
        addMouseWheelListener(new mouseClick());

        //Adding keyboard Buttons listeners to the Board Panel to listen to actions and events triggered by the keyboard
        addKeyListener(new keyboardIn());

        //Calling the score JLabel from the Tetris Object joc, parameter called in the constructor declaration of the function
        score = joc.score;

        //Making all the squares in the grid NoShape(making the Tetris Grid Empty)
        clearBoard();
    }

    //Method which calls what will happen when an action is performed.
    //It Calls a new Tetris Piece if the current piece cant advance anymore or if it can advance it lowers the current piece one more line.
    public void actionPerformed(ActionEvent e) {
        if (cadereTerminata) {
            cadereTerminata = false;
            newForma();
        } else {
            coboaraLinie();
        }
    }


    //Method which starts the Gameplay session.
    //It cleares the grid, starts the timer and creates a new Tetris Piece at the middle of the first upper row.
    public void pornire() {

        Pornit = true;
        cadereTerminata = false;
        liniiScoase = 0;
        spaceuri=0;
        score.setText( " Score: 0" );
        clearBoard();
        timp.setDelay(400);
        newForma();
        timp.start();
    }

    //Method which returns the width of a square in the grid
    int squareWidth() {
        return (int) getSize().getWidth() / 10;
    }

    //Method which returns the height of a square in the grid
    int squareHeight() {
        return (int) getSize().getHeight() / 20;
    }

    //Method which paints the squares of the grid and the Tetris Pieces
    //First it paints the Tetris Grid(Board)
    //Second it Paints the current Tetris Piece(the Squares in the Grid by using paintingSquares Method)
    public void paint(Graphics g) {
        super.paint(g);
        Dimension marime = getSize();
        int masaTop = (int) marime.getHeight() - 20 * squareHeight();


        for (int i = 0; i < 20; ++i) {
            for (int j = 0; j < 10; ++j) {
                Shape.Piesa shape = masa[((20 - i - 1) * 10) + j];
                if (shape != Shape.Piesa.NoForma)
                    paintingSquares(g, 0 + j * squareWidth(), masaTop + i * squareHeight(), shape);
            }
        }

        if (curForma.getForma() != Shape.Piesa.NoForma) {
            int i = 0;
            while (i < 4) {
                int x = curX + curForma.coords[i][0];
                int y = curY - curForma.coords[i][1];
                paintingSquares(g, 0 + x * squareWidth(), masaTop + (Linii - y - 1) * squareHeight(), curForma.getForma());
                i += 1;
            }
        }
    }


    //Method to make the Tetris Piece fall if it can and there is no other square on the next line blocking the path of the piece
    private void coboaraLinie() {
        if (!incearcaMiscare(curForma, curX, curY - 1))
            piesaCazuta();
    }

    //Method to clear all the squares in the Tetris Grid making them NoForma
    private void clearBoard() {
        int i = 0;
        while (i < 20 * 10) {
            masa[i] = Shape.Piesa.NoForma;
            i += 1;
        }
    }

    //Method which Creates a new Tetris Piece if the current piece finished falling and touches the ground or another piece
    private void piesaCazuta() {
        for (int i = 0; i < 4; ++i) {
            int x = curX + curForma.coords[i][0];
            int y = curY - curForma.coords[i][1];
            masa[(y * 10) + x] = curForma.getForma();
        }

        stergeLiniiPline();

        if (!cadereTerminata)
            newForma();
    }

    //Method which creates a new random Piece and checks if the game has finished or not
    //If the piece which is created can not advance because the path is blocked by another piece/shape/square the game
    //Or another piece is where it spawns, the game is over and a message is prompted in the score JLabel
    private void newForma() {
        curForma.formaRandom();
        curX = 10 / 2;
        curY = 20 - 1 + curForma.posMin();

        if (!incearcaMiscare(curForma, curX, curY)) {
            curForma.seteazaForma(Shape.Piesa.NoForma);
            timp.stop();
            Pornit = false;
            score.setText(" Score: " + String.valueOf(liniiScoase * 10+spaceuri) + "  GAME OVER!   PLAY AGAIN? (Y/N) ");
        }
    }

    //Method which checks if the current Tetris Piece can advance and repaints if it is possible returning true
    private boolean incearcaMiscare(Shape newForma, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newForma.coords[i][0];
            int y = newY - newForma.coords[i][1];
            if (x < 0 || x >= 10 || y < 0 || y >= Linii)
                return false;
            if (masa[(y * 10) + x] != Shape.Piesa.NoForma)
                return false;
        }

        curForma = newForma;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    //Method which deletes the full lines
    //Also this method is incresing the score ammount and displays the new score
    //The feature added (Regarding the assignment request) is here, incresing the score like this
    // If the lines removed are higher than 1, the score added is multiplied by the number of lines removed creating a COMBO
    private void stergeLiniiPline() {
        int liniiPline = 0;

        for (int i = Linii - 1; i >= 0; --i) {
            boolean liniePlina = true;

            for (int j = 0; j < 10; ++j) {
                if (masa[(i * 10) + j] == Shape.Piesa.NoForma) {
                    liniePlina = false;
                    break;
                }
            }

            if (liniePlina) {
                ++liniiPline;
                //Adding speed regarding the score the user has
                timp.setDelay(timp.getDelay()-10);
                for (int t = i; t < 20 - 1; ++t) {
                    for (int y = 0; y < 10; ++y)
                        masa[(t * 10) + y] = masa[((t + 1) * 10) + y];
                }
            }
        }

        if (liniiPline > 0) {
            if (liniiPline > 1) {
                liniiScoase += liniiPline * liniiPline;
                score.setText(" Score: " + String.valueOf(liniiScoase * 10+spaceuri) + "         " + "   COMBO x" + liniiPline);
            } else {
                liniiScoase += liniiPline;
                score.setText(" Score: " + String.valueOf(liniiScoase * 10+spaceuri));
            }
            cadereTerminata = true;
            curForma.seteazaForma(Shape.Piesa.NoForma);
            repaint();
        }
    }

    //Methos which paints the Squares of each shape in different colours, regarding the ordinal number of the Tetris Piece in the enum Piesa
    private void paintingSquares(Graphics g, int x, int y, Shape.Piesa Forma) {
        Color culori[] = {

                new Color(0, 0, 0),
                new Color(204, 0, 27),
                new Color(0, 204, 27),
                new Color(0, 10, 204),
                new Color(204, 204, 0),
                new Color(204, 0, 186),
                new Color(0, 200, 204),
                new Color(218, 131, 91)
        };


        Color color = culori[Forma.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(new Color(0x000000));
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
    }

    //Class which extend MouseAdapter and says what and how the mouse actions should execute and behave
    class mouseClick extends MouseAdapter {

        //Method which overrides the mouse buttons events
        //If the left mouse button (BUTTON1) is pressed the Tetris Piece moves to left
        //If the right mouse button (BUTTON3) is pressed the Tetris Piece moves to right
        public void mouseClicked(MouseEvent e) {

            if (!Pornit || curForma.getForma() == Shape.Piesa.NoForma) {
                return;
            }

            int clickPressed = e.getButton();


            if (clickPressed == MouseEvent.BUTTON1)
                incearcaMiscare(curForma, curX - 1, curY);

            if (clickPressed == MouseEvent.BUTTON3)
                incearcaMiscare(curForma, curX + 1, curY);
        }

        //Method which overrides the mouse wheel events
        //If the mouse wheel rotates upwards the Tetris Piece Rotates to right or clockwise
        //If the mouse wheel rotates downwards the Tetris Piece Rotates to left or counter clockwise
        public void mouseWheelMoved(MouseWheelEvent ea) {
            if (!Pornit || curForma.getForma() == Shape.Piesa.NoForma) {
                return;
            }

            int notches = ea.getWheelRotation();
            if (notches < 0)
                incearcaMiscare(curForma.rotireDreapta(), curX, curY);
            else
                incearcaMiscare(curForma.rotireStanga(), curX, curY);
        }
    }

    //Class which extends KeyAdapter that takes key imput from the keyboard giving functionality to the tetris game
    class keyboardIn extends KeyAdapter {

        //Mehod overriding the pressed keys from the keyboard
        public void keyPressed (KeyEvent eee) {

            int buttonApasat = eee.getKeyCode();

            //Functionality for the end of the game
            if(Pornit==false){
                if (buttonApasat == 'y' || buttonApasat == 'Y')
                    pornire();
                if (buttonApasat == 'n' || buttonApasat == 'N')
                    joc.dispatchEvent(new WindowEvent(joc, WindowEvent.WINDOW_CLOSING));
            }

            //Lowering Tetris Pieces faster if the Spacebar is pressed
            if(buttonApasat == KeyEvent.VK_SPACE)
            {

                //The score is increasing by 1 point with every Space Bar pressed
                if (Pornit==true){
                    spaceuri+=1;
                    score.setText(" Score: " + String.valueOf(liniiScoase*10+spaceuri));
                    coboaraLinie();
                }
            }

            //Implemented functionality for the arrow keys
            if (Pornit==true)
            {
                if (buttonApasat == KeyEvent.VK_LEFT)
                {
                    incearcaMiscare(curForma, curX - 1, curY);
                }

                if (buttonApasat == KeyEvent.VK_RIGHT)
                {
                    incearcaMiscare(curForma, curX + 1, curY);
                }
                if(buttonApasat == KeyEvent.VK_UP)
                {
                    incearcaMiscare(curForma.rotireDreapta(), curX, curY);
                }
                if(buttonApasat == KeyEvent.VK_DOWN)
                {
                    incearcaMiscare(curForma.rotireStanga(), curX, curY);
                }
            }

        }
    }
}

/**
 * Created by Liviu-Marius Miu on 02/12/2016.
 * Reg. No:1502419
 */