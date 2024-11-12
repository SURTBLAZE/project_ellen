package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class TimeBomb extends AbstractActor {
    private float time;
    private boolean state;
    private Animation animation;
    public TimeBomb(float time){
        animation = new Animation("sprites/bomb.png",16,16);
        setAnimation(animation);
        this.time = time;
        this.state = false;
    }

    private void explosion(){
        animation = new Animation("sprites/small_explosion.png",16,16,0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
        new When<>(
            () -> {
                return this.animation.getCurrentFrameIndex() == 7;
            },
            new Invoke<>(() -> {
                Scene scene = getScene();
                if(scene != null){
                    scene.removeActor(this);
                }
            })
        ).scheduleFor(this);
        if(this instanceof ChainBomb) ((ChainBomb) this).activateAll();
    }

    public void activate(){
        animation = new Animation("sprites/bomb_activated.png",16,16,0.2f,Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
        this.state = true;
        new ActionSequence<>(
            new Wait<>(time),
            new Invoke<>(this::explosion)
        ).scheduleFor(this);
    }

    public boolean isActivated() {
        return state;
    }
}
