package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;

public abstract class BreakableTool<A extends Actor> extends AbstractActor implements Usable<A>{
    private int remainingUses;

    protected BreakableTool(int remainingUses){
        this.remainingUses = remainingUses;
    }

    public void useWith(A actor){
        remainingUses--;
        if(remainingUses < 1){
            Scene scene = getScene();
            if(scene != null){
                scene.removeActor(this);
            }
        }
    }
    protected int getRemainingUses() {
        return remainingUses;
    }

    protected void setRemainingUses(int remainingUses) {
        this.remainingUses = remainingUses;
    }
}
