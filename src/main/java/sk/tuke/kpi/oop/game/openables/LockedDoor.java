package sk.tuke.kpi.oop.game.openables;

import sk.tuke.kpi.gamelib.Actor;

public class LockedDoor extends Door{
    private boolean state;
    public LockedDoor(String name,Orientation orientation){
        super(name,orientation);
        this.state = true;
    }

    @Override
    public void useWith(Actor actor) {
        if(isLocked()) return;
        super.useWith(actor);
    }

    public void lock(){
        if(!isLocked()){
            this.close();
            this.state = true;
        }
    }

    public void unlock(){
        if(isLocked()){
            this.open();
            this.state = false;
        }
    }

    public boolean isLocked(){
        return this.state;
    }
}
