package Model;

/**
 * Created by Михаил on 11.12.2016.
 */
public class Cell {
    public final int x;


    public final int y;

    public int getState() {
        return state;
    }

    public void switchState() {
        state = (state == 1)? 0:1;
    }
    public void setState( int s) {
        state = s;
    }

    private int state;

    public Cell(int _x , int _y)
    {
        x = _x;
        y = _y;
        state = 0;
    }

}
