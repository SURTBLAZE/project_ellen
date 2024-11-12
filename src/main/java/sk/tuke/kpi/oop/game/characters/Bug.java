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
import sk.tuke.kpi.oop.game.items.AccessCard;

import java.util.List;

public class Bug extends AbstractActor implements Enemy,Alive, Movable {
    private Health health;
    private Disposable bugBehavior; //attack
    private Animation animation;
    private Behaviour<? super Bug> behaviour;
    private String cardName;

    public Bug() {
        health = new Health(200);
        this.behaviour = new RandomlyMoving();
        animation = new Animation("sprites/lurker_alien.png",32,32,0.2f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
        this.animation.stop();
        bugBehavior = null;
        health.onFatigued(() -> {
            if(getScene() == null) return;
            getScene().removeActor(this);
        });
    }

    public Bug(String cardName) {
        this.cardName = cardName;
        health = new Health(200);
        this.behaviour = new RandomlyMoving();
        animation = new Animation("sprites/lurker_alien.png",32,32,0.2f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
        this.animation.stop();
        bugBehavior = null;
        health.onFatigued(() -> {
            if(getScene() == null) return;
            if(this.getCardName().equals("storage door")){
                getScene().addActor(new AccessCard(this.getCardName()),this.getPosX(),this.getPosY());
            }
            getScene().removeActor(this);
        });
    }
    @Override
    public int getSpeed() {
        return 2;
    }
    @Override
    public Health getHealth() {
        return health;
    }

    @Override
    public void startedMoving(Direction direction) {
        Movable.super.startedMoving(direction);
        this.animation.setRotation(direction.getAngle());
        this.animation.play();
    }

    public String getCardName() {
        return cardName;
    }

    @Override
    public void stoppedMoving() {
        Movable.super.stoppedMoving();
        this.animation.stop();
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        setBugBehavior();
        if(behaviour == null) return;
        behaviour.setUp(this);
    }

    public void attack(){
        if(getScene() == null) return;
        List<Actor> actors = getScene().getActors();
        for(Actor actor : actors){
            if((actor instanceof Alive) && !(actor instanceof Enemy) && actor.intersects(this)){
                ((Alive) actor).getHealth().drain(10);
                new ActionSequence<>(
                    new Invoke<>(this::stopAttack),
                    new Wait<>(0.9f),
                    new Invoke<>(this::setBugBehavior)
                ).scheduleFor(this);
            }
        }
    }

    public void setBugBehavior(){
        this.bugBehavior = new Loop<>(new ActionSequence<>(
            new Invoke<>(this::attack),
            new Wait<>(0.8f)
        )).scheduleFor(this);
    }

    public void stopAttack(){
        if(this.bugBehavior == null) return;
        this.bugBehavior.dispose();
        this.bugBehavior = null;
    }

}
