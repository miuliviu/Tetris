/**
 * Created by Liviu-Marius Miu on 02/12/2016..
 * Reg. No:1502419
 */

import java.awt.*;

import javax.swing.*;

/**
 * Tetris Class extends the JFrame Class and has a constructor which starts the game and makes the session visible
 * Also it creates a score JLabel which displays the score and Combo messages or Game Over message
 * */
public class Tetris extends JFrame {

    //Creating the score Label which can be called by the Board Class and changed during the session
    JLabel score;


    public Tetris() {
        //Edditing the visual characteristics of the score Jlabel and adding it to the Tetris Frame
        score = new JLabel(" Score: 0");
        score.setFont(new Font("Calibri", Font.ITALIC, 20));
        score.setBorder(BorderFactory.createBevelBorder(0 ,Color.RED , Color.ORANGE));
        add(score, BorderLayout.NORTH);

        //Creating a Board Object(The Tetris Game) and adding it to the Tetris Frame
        //Also calling the method pornire() which starts the game session
        Board game = new Board(this);
        game.setBorder(BorderFactory.createLineBorder(Color.black));
        game.setBackground(Color.gray);
        add(game);
        game.pornire();

        //Setting size of the frame, the title of the frame regarding the assignment request
        //Also making the frame non-resizable and creating the default closing operation
        setSize(396, 600);
        setTitle("Liviu-Marius Miu - CE203 - 1502419");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}

/**
 * Created by Liviu-Marius Miu on 02/12/2016.
 * Reg. No:1502419
 */