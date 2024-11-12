package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Repairable;

public class Hammer extends BreakableTool<Repairable> implements Collectible {
    public Hammer(){
        super(1);
        Animation animation = new Animation("sprites/hammer.png",16,16);
        setAnimation(animation);
    }
    @Override
    public void useWith(Repairable actor){
        if(actor == null || !actor.repair()) return;
        this.setRemainingUses(this.getRemainingUses() - 1);
        if(this.getRemainingUses() < 1){
            Scene scene = getScene();
            if(scene != null){
                scene.removeActor(this);
            }
        }
    }

    @Override
    public Class<Repairable> getUsingActorClass() {
        return Repairable.class;
    }
}
