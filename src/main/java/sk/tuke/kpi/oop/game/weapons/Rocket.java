package sk.tuke.kpi.oop.game.weapons;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.util.List;

public class Rocket extends AbstractActor implements Fireable {
    private Animation animation;
    private Animation explosionAnimation;

    private Disposable behavior;

    public Rocket(){
        animation = new Animation("sprites/rocket.png",16,16);
        explosionAnimation = new Animation("sprites/large_explosion.png",32,32,0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        setRocketBehavior();
    }

    public void setRocketBehavior(){
        this.behavior = new Loop<>(new Invoke<>(this::kill)).scheduleFor(this);
    }

    public void kill(){
        if(getScene() == null) return;
        List<Actor> actors = getScene().getActors();
        for(Actor actor : actors){
            if(actor instanceof Alive && actor.intersects(this)){
                if(actor instanceof Ripley) continue;
                ((Alive) actor).getHealth().drain(100);
                Rocket newRocket = new Rocket(); //rocket for explosion
                getScene().addActor(newRocket,this.getPosX(),this.getPosY());
                newRocket.explosion();
                getScene().removeActor(this);
            }
        }
    }

    private void explosion(){
        this.behavior.dispose();
        setAnimation(explosionAnimation);
        this.stoppedMoving();
        new When<>(
            () -> {
                return this.explosionAnimation.getCurrentFrameIndex() == 15;
            },
            new Invoke<>(() -> {
                Scene scene = getScene();
                if(scene != null){
                    scene.removeActor(this);
                }
            })
        ).scheduleFor(this);
    }

    public void collidedWithWall() {
        Fireable.super.collidedWithWall();
        if(getScene() == null) return;
        Rocket newRocket = new Rocket(); //rocket for explosion
        getScene().addActor(newRocket,this.getPosX(),this.getPosY());
        newRocket.explosion();
        getScene().removeActor(this);
    }

    @Override
    public int getSpeed() {
        return 3;
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
