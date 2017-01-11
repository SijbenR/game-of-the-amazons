package group4.ui;

public class GridCoordinate {

    public int x;
    public int y;

    public GridCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isSameGridcoordinate(GridCoordinate comp) {


        if(comp.x == x && comp.y == y) {
            //System.out.println("returning true");
            return true;
        }

        //System.out.println("returning false");
        return false;
    }

    public String toString()    {
        return new String("X = " + x + " | Y = " + y + "\n");
    }

}
