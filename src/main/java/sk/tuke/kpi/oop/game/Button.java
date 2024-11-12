package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.items.Usable;

public class Button extends AbstractActor implements Usable<Actor> {
    private Animation onAnimation;
    private Animation offAnimation;
    private PowerSwitch powerSwitch;
    private boolean isPressed;
    private String name;
    public Button(String name){
        this.onAnimation = new Animation("sprites/button_green.png",16,16);
        this.offAnimation = new Animation("sprites/button_red.png",16,16);
        setAnimation(offAnimation);
        this.isPressed = false;
        this.name = name;
        this.powerSwitch = new PowerSwitch(null);
    }

    public void press(){
        if(powerSwitch == null) return;
        if(!this.isPressed){
            setAnimation(onAnimation);
            isPressed = true;
        }
        else{
            setAnimation(offAnimation);
            isPressed = false;
        }
        powerSwitch.toggle();
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    public void setSwitchable(Switchable switchable){
        this.powerSwitch.setDevice(switchable);
    }

    @Override
    public void useWith(Actor actor) {
        this.press();
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }
}
