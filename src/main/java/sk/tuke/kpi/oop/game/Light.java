package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Light extends AbstractActor implements Switchable,EnergyConsumer{
    private boolean state;
    private boolean electricityFlow;
    private Animation onAnimation;
    private Animation offAnimation;
    public Light(){
        onAnimation = new Animation("sprites/light_on.png");
        offAnimation = new Animation("sprites/light_off.png");
        state = false;
        electricityFlow = false;
        setAnimation(offAnimation);
    }

    public void toggle(){
        if(!state){
            this.turnOn();
        }
        else{
            this.turnOff();
        }
        updateAnimation();
    }

    private void updateAnimation(){
        if(state && electricityFlow){
            setAnimation(onAnimation);
        }
        else {
            setAnimation(offAnimation);
        }
    }

    public void setElectricityFlow(boolean electricityFlow){
        this.electricityFlow = electricityFlow;
        updateAnimation();
    }

    public void setState(boolean state) {
        this.state = state;
        updateAnimation();
    }

    public boolean isElectricityFlow() {
        return electricityFlow;
    }

    @Override
    public void turnOn() {
        this.state = true;
        updateAnimation();
    }

    @Override
    public void turnOff() {
        this.state = false;
        updateAnimation();
    }

    @Override
    public boolean isOn() {
        return state;
    }

    @Override
    public void setPowered(boolean power) {
        this.setElectricityFlow(power);
    }
}
