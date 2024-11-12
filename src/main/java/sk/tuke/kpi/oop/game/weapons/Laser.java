package sk.tuke.kpi.oop.game.weapons;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.util.List;

public class Laser extends AbstractActor implements Fireable{
    private Animation animation;
    private Disposable behavior;
    private int speed;
    public Laser(Animation animation,int speed){
        this.animation = animation;
        setAnimation(animation);
        this.speed = speed;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        setLaserBehavior();
    }

    public void setLaserBehavior(){
        this.behavior = new Loop<>(new Invoke<>(this::kill)).scheduleFor(this);
    }

    public void kill(){
        if(getScene() == null) return;
        List<Actor> actors = getScene().getActors();
        for(Actor actor : actors){
            if(actor instanceof Alive && actor.intersects(this)){
                if(actor instanceof Ripley) continue;
                ((Alive) actor).getHealth().drain(30);
                getScene().removeActor(this);
            }
        }
    }

    public void collidedWithWall() {
        Fireable.super.collidedWithWall();
        if(getScene() == null) return;
        getScene().removeActor(this);
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public void startedMoving(Direction direction) {
        Fireable.super.startedMoving(direction);
        animation.setRotation(direction.getAngle());
        animation.play();
    }

    @Override
    public void stoppedMoving() {
        Fireable.super.stoppedMoving();
    }
}
