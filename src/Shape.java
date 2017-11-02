/**
 * Created by Liviu-Marius Miu on 02/12/2016.
 * Reg. No:1502419
 */
import java.util.Random;
import java.lang.Math;

/**
 * Shape Class is the class where the Shape for the Tetris piece is created and retruned.
 * The shapes are saved in a 2D Array which are indented by an enum variable called Piesa.
 * Also the enum Piesa, says which color each piece has and what coords they have in the coordsMasa aray.
 */


public class Shape {

    //Enum which holds the names of all the tetris Pieces/Forms
    enum Piesa { NoForma, ZForma, SForma, IForma, TForma, PatratForma, LForma, LIntorsForma };

    //Creating Piesa variable in which we save the shape of the piece
    private Piesa formaPiesei;

    //Creating a 2D Array which holds the coords of the shape
    public int coords[][];

    //Creating a 2D Array which holds all the coords of all the Shapes
    private int[][] coordsMasa;

    //Shape Constructor
    public Shape() {

        //Giving to the variable coords a 2D array with 4 columns and 2 lines for the current piece and setting the shape to NoForm
        coords = new int[4][2];
        seteazaForma(Piesa.NoForma);

    }

    //Method which sets the desired shape regarding the chosen ordinal of the Enum Piesa
    public void seteazaForma(Piesa forma) {

        //2D array holding all the shapes of the tetris pieces
        coordsMasa = new int[][] {
                {  0, 0, 0, 0, 0, 0, 0, 0 } ,
                { 0, -1, 0, 0, -1, 0, -1, 1 },
                { 0, -1, 0, 0, 1, 0, 1, 1 },
                {  0, -1, 0, 0,  0, 1,  0, 2  },
                {  -1, 0,  0, 0, 1, 0, 0, 1  },
                {  0, 0, 1, 0, 0, 1, 1, 1  },
                {  -1, -1, 0, -1, 0, 0, 0, 1 },
                { 1, -1,  0, -1, 0, 0,  0, 1 }
        };

        //Saving the coords of the current chosen shape
        int iter =0;
        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = coordsMasa[forma.ordinal()][iter];
                iter+=1;
            }
        }

        //Saving the shape in a variable which will return the shape to the Board class to be painted in the board
        formaPiesei = forma;

    }

    //Method to return the shape of the tetris piece
    public Piesa getForma()
    {
        return formaPiesei;
    }

    //Method to set a random shape for the tetris Shape
    public void formaRandom()
    {
        Random r = new Random();
        int x =1 + Math.abs(r.nextInt()) % 7;
        Piesa[] valoare = Piesa.values();
        seteazaForma(valoare[x]);
    }

    //Method which returns the minimum position of the Tetris Shape (The lowest square of the shape)
    public int posMin()
    {
        int min = coords[0][1];
        int i =0;
        while(i<4) {
            if(coords[i][1]<min)
                min=coords[i][1];
            i+=1;
        }
        return min;
    }

    //Method to rotate the shape counter cloackwise
    public Shape rotireStanga()
    {
        if (formaPiesei == Piesa.PatratForma)
            return this;

        Shape formaNoua = new Shape();
        formaNoua.formaPiesei = formaPiesei;

        int i=0;
        while(i<4) {
            formaNoua.coords[i][0] = coords[i][1];
            formaNoua.coords[i][1]= -coords[i][0];
            i+=1;
        }
        return formaNoua;
    }

    //Method to rotate the shape Clockwise
    public Shape rotireDreapta()
    {
        if (formaPiesei == Piesa.PatratForma)
            return this;

        Shape formaNoua = new Shape();
        formaNoua.formaPiesei = formaPiesei;
        int i=0;
        while(i<4){
            formaNoua.coords[i][0]= -coords[i][1];
            formaNoua.coords[i][1]= coords[i][0];
            i+=1;
        }
        return formaNoua;
    }
}

/**
 * Created by Liviu-Marius Miu on 02/12/2016.
 * Reg. No:1502419
 */