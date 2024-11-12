package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;

public class MotherAlien extends Alien{
    private Health health;
    private Animation animation;
    private Animation animationImmortal;
    private Disposable immortality;

    public static final Topic<MotherAlien> MOTHER_ALIEN_DEAD = Topic.create("mother alien dead", MotherAlien.class);
    public MotherAlien(int healthpoints,Behaviour<? super Alien> behaviour){
        super(healthpoints,behaviour);
        this.health = new Health(10000);
        animation = new Animation("sprites/mother.png",112,162,0.2f, Animation.PlayMode.LOOP_PINGPONG);
        animationImmortal = new Animation("sprites/mother_immortal.png",112,162,0.2f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
        animation.stop();
    }

    private boolean findChildren(){
        if(getScene() == null) return false;
        for(Actor actor : getScene().getActors()){
            if(actor instanceof Alien && !(actor instanceof MotherAlien)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addedToScene(Scene scene){
        if(scene == null) return;
        super.addedToScene(scene);
        this.immortality = new Loop<>(new ActionSequence<>(
            new Invoke<>(() -> {
                if(this.findChildren()){
                    setAnimation(animationImmortal);
                    this.getHealth().restore();
                }
                else{
                    if(this.getHealth().getValue() > 500) {
                        this.getHealth().setValue(500);
                        setAnimation(animation);
                    }
                }
            })
        )).scheduleFor(this);
    }

    @Override
    public void startedMoving(Direction direction) {
        super.startedMoving(direction);
        this.animation.setRotation(direction.getAngle());
        this.animation.play();
    }

    @Override
    public void stoppedMoving() {
        super.stoppedMoving();
        this.animation.stop();
    }
}
