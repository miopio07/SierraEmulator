package sierra.habbohotel.pathfinder;

public class Coord
{
    public int X;
    public int Y;
    
    public int PositionDistance;
    public int ReversedPositionDistance;

    public Coord(int x, int y)
    {
        X = x;
        Y = y;
        PositionDistance = 1000;
        ReversedPositionDistance = 1000;
    }
}
