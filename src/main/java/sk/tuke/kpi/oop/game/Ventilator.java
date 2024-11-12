package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.openables.Door;

public class Ventilator extends AbstractActor implements Repairable {
    private Animation animation;
    private boolean isBroken;
    public static final Topic<Ventilator> VENTILATOR_REPAIRED = Topic.create("ventilator repaired", Ventilator.class);
    public Ventilator(){
        animation = new Animation("sprites/ventilator.png",32,32,0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
        isBroken = false;
    }

    public Ventilator(boolean isBroken){
        animation = new Animation("sprites/ventilator.png",32,32,0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
        this.isBroken = isBroken;
        if(isBroken) this.stop();
    }
    @Override
    public boolean repair() {
        if(isOn()) return false;
        animation.play();
        getScene().getMessageBus().publish(VENTILATOR_REPAIRED,this);
        this.isBroken = false;
        return true;
    }

    public void stop(){
        getAnimation().stop();
    }

    public boolean isOn(){
        return !isBroken;
    }
}
