package sk.tuke.kpi.oop.game.weapons;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.characters.Alive;

import java.util.List;

public class Bullet extends AbstractActor implements Fireable {
    private Animation animation;
    public Bullet(){
        animation = new Animation("sprites/bullet.png",16,16);
        setAnimation(animation);
    }
    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        setBulletBehavior();
    }

    public void setBulletBehavior(){
        new Loop<>(new Invoke<>(this::kill)).scheduleFor(this);
    }

    public void kill(){
        if(getScene() == null) return;
        List<Actor> actors = getScene().getActors();
        for(Actor actor : actors){
            if(actor instanceof Alive && actor.intersects(this)){
                ((Alive) actor).getHealth().drain(15);
                //System.out.println(actor + " : health: " + ((Alive) actor).getHealth().getValue());
                getScene().removeActor(this);
            }
        }
    }

    @Override
    public void collidedWithWall() {
        Fireable.super.collidedWithWall();
        if(getScene() == null) return;
        getScene().removeActor(this);
    }

    @Override
    public int getSpeed() {
        return 4;
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
