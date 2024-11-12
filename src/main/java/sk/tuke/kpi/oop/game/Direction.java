package sk.tuke.kpi.oop.game;

public enum Direction {
    NORTH(0,1),
    EAST(1,0),
    SOUTH(0,-1),
    WEST(-1,0),
    NORTHWEST(-1,1),
    NORTHEAST(1,1),
    SOUTHWEST(-1,-1),
    SOUTHEAST(1,-1),
    NONE(0,0);

    private final int dx;
    private final int dy;
    private int degree;

    static {
        NORTH.degree =    0;
        EAST.degree =     270;
        SOUTH.degree =    180;
        WEST.degree =     90;
        NORTHEAST.degree= 315;
        SOUTHEAST.degree= 225;
        SOUTHWEST.degree= 135;
        NORTHWEST.degree= 45;
    }

    Direction(int dx,int dy){
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public Direction combine(Direction other) {
        if (other == null) return Direction.NONE;
        int dx, dy;
        if(this.dx == other.dx) {
            dx = this.dx;
        }
        else {dx = this.dx + other.dx;}
        if(this.dy == other.dy) {
            dy = this.dy;
        }
        else {dy = this.dy + other.dy;}

        for(Direction direction : Direction.values()){
            if(direction.dx == dx && direction.dy == dy){
                return direction;
            }
        }
        return this;
    }

    public float getAngle(){
       /* if(dx == 0 && dy == -1){
            return 180;
        }
        else if(dx == 1 && dy == 0){
            return 270;
        }
        else if(dx == -1 && dy == 0){
            return 90;
        }
        else if(dx == -1 && dy == 1){
            return 45;
        }
        else if(dx == 1 && dy == 1){
            return 315;
        }
        else if(dx == -1 && dy == -1){
            return 135;
        }
        else if(dx == 1 && dy == -1){
            return 225;
        }*/
        return degree;
    }

    public static Direction fromAngle(float angle){
        if(angle == 45){
            return Direction.NORTHWEST;
        }
        if(angle == 90){
            return Direction.WEST;
        }
        if(angle == 135){
            return Direction.SOUTHWEST;
        }
        if(angle == 180){
            return Direction.SOUTH;
        }
        if(angle == 225){
            return Direction.SOUTHEAST;
        }
        if(angle == 270){
            return Direction.EAST;
        }
        if(angle == 315){
            return Direction.NORTHEAST;
        }
        return Direction.NORTH;
    }
}
