package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.weapons.Bazooka;
import sk.tuke.kpi.oop.game.weapons.Firearm;
import sk.tuke.kpi.oop.game.weapons.Gausgun;
import sk.tuke.kpi.oop.game.weapons.Gun;

public class Ripley extends AbstractActor implements Movable, Keeper,Alive,Armed {
    private Animation animation;
    private Health health;
    private Firearm weapon;
    private Backpack backpack;

    public static final Topic<Ripley> RIPLEY_DIED = Topic.create("ripley died", Ripley.class);

    public Ripley(){
        super("Ellen");
        this.health = new Health(100);
        //this.weapon = new Gun(100,120);
        this.weapon = new Gausgun(200,200);
        backpack = new Backpack("Ripley's backpack",3);
        animation = new Animation("sprites/player.png",32,32,0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
        animation.stop();
        health.onFatigued(() -> {
            this.animation = new Animation("sprites/player_die.png",32,32,0.1f, Animation.PlayMode.ONCE);
            setAnimation(animation);
            getScene().getMessageBus().publish(RIPLEY_DIED,this);
        });
    }

    public void setWeapon(Firearm firearm){
        if(firearm == null) return;
        this.weapon = firearm;
    }

    //--------------------------------------------
    //              sourcemaking.com - design patterns
    //--------------------------------------------
    @Override
    public int getSpeed() {
        return 2;
    }

    @Override
    public void startedMoving(Direction direction) {
        Movable.super.startedMoving(direction);
        animation.setRotation(direction.getAngle());
        animation.play();
    }

    @Override
    public void stoppedMoving() {
        Movable.super.stoppedMoving();
        animation.stop();
    }

    @Override
    public Backpack getBackpack() {
        return this.backpack;
    }

    public void showRipleyState(){
        int windowHeight = getScene().getGame().getWindowSetup().getHeight();
        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;
        getScene().getGame().getOverlay().drawText("Energy: " + this.health.getValue(),110,yTextPos);
    }

    @Override
    public Health getHealth() {
        return this.health;
    }

    @Override
    public Firearm getFirearm() {
        return this.weapon;
    }

    @Override
    public void setFirearm(Firearm weapon) {
        this.weapon = weapon;
    }
}
