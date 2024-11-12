package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Reactor;

public class FireExtinguisher extends BreakableTool<Reactor> implements Collectible{

    public FireExtinguisher(){
        super(1);
        Animation animation = new Animation("sprites/extinguisher.png",16,16);
        setAnimation(animation);
    }
    @Override
    public void useWith(Reactor reactor){
        if(reactor == null || !reactor.extinguish()) return;
        this.setRemainingUses(this.getRemainingUses() - 1);
        if(this.getRemainingUses() < 1){
            Scene scene = getScene();
            if(scene != null){
                scene.removeActor(this);
            }
        }
    }

    @Override
    public Class<Reactor> getUsingActorClass() {
        return Reactor.class;
    }
}
