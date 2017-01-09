package group4.ui;

public class GridCoordinate {

    public int x;
    public int y;

    public GridCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isSame(GridCoordinate comp) {
        if(comp.x == x && comp.y == y)
            return true;

        return false;
    }

    public String toString()    {
        return new String("X = " + x + " | Y = " + y + "\n");
    }

}
