package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;

import java.util.List;

public class Alien extends AbstractActor implements Enemy,Alive, Movable {
    private Health health;
    private Disposable alienBehavior; //attack
    private Animation animation;
    private Behaviour<? super Alien> behaviour;

    public Alien() {
        health = new Health(100);
        this.behaviour = new RandomlyMoving();
        animation = new Animation("sprites/infected.png",32,32,0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
        this.animation.stop();
        alienBehavior = null;
        health.onFatigued(() -> {
            if(getScene() == null) return;
            getScene().removeActor(this);
        });
    }

    public Alien(int healthValue,Behaviour<? super Alien> behaviour){
        health = new Health(healthValue);
        this.behaviour = behaviour;
        animation = new Animation("sprites/infected.png",32,32,0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
        this.animation.stop();
        alienBehavior = null;
        health.onFatigued(() -> {
            if(getScene() == null) return;
            if(this instanceof MotherAlien){
                getScene().getMessageBus().publish(MotherAlien.MOTHER_ALIEN_DEAD,(MotherAlien) this);
            }else {
                getScene().removeActor(this);
            }
        });
    }
    @Override
    public Health getHealth() {
        return this.health;
    }
    @Override
    public int getSpeed(){
        return 1;
    }

    @Override
    public void startedMoving(Direction direction) {
        Movable.super.startedMoving(direction);
        this.animation.setRotation(direction.getAngle());
        this.animation.play();
    }

    @Override
    public void stoppedMoving() {
        Movable.super.stoppedMoving();
        this.animation.stop();
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        setAlienBehavior();
        if(behaviour == null) return;
        behaviour.setUp(this);
    }

    public void attack(){
        if(getScene() == null) return;
        List<Actor> actors = getScene().getActors();
        for(Actor actor : actors){
            if((actor instanceof Alive) && !(actor instanceof Enemy) && actor.intersects(this)){
                ((Alive) actor).getHealth().drain(2);
                new ActionSequence<>(
                    new Invoke<>(this::stopAttack),
                    new Wait<>(0.5f),
                    new Invoke<>(this::setAlienBehavior)
                ).scheduleFor(this);
            }
        }
    }

    public void setAlienBehavior(){
        this.alienBehavior = new Loop<>(new ActionSequence<>(
            new Invoke<>(this::attack),
            new Wait<>(0.3f)
        )).scheduleFor(this);
    }

    public void stopAttack(){
        if(this.alienBehavior == null) return;
        this.alienBehavior.dispose();
        this.alienBehavior = null;
    }


}
