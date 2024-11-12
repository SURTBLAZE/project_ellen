package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Cooler extends AbstractActor implements Switchable{

    private boolean state;
    private Animation animation;
    private Reactor reactor;
    public Cooler(Reactor reactor){
        animation = new Animation("sprites/fan.png",32,32,0.2f,Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
        animation.stop();
        this.reactor = reactor;
        state = false;
    }

    private void coolReactor(){
        if(reactor != null && isOn()){
            if(reactor.getDamage() >= 50) {
                reactor.decreaseTemperature(2);
            }
            else {
                reactor.decreaseTemperature(1);
            }
        }
    }

    @Override
    public void addedToScene(Scene scene){
        if(scene == null) return;

        super.addedToScene(scene);
        new Loop<>(new Invoke<>(Cooler::coolReactor)).scheduleFor(this);
        //scene.scheduleAction(new Loop<>(new Invoke<>(this::coolReactor)),this);
    }

    @Override
    public void turnOn(){
        state = true;
        animation.play();
    }

    @Override
    public void turnOff(){
        state = false;
        animation.stop();
    }

    public Reactor getReactor() {
        return reactor;
    }

    public void setReactor(Reactor reactor) {
        this.reactor = reactor;
    }

    @Override
    public boolean isOn(){
        return state;
    }
}
