package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Color;

public class Computer extends AbstractActor implements EnergyConsumer{
    private boolean electricityFlow;

    public Computer(){
        Animation normalAnimation = new Animation("sprites/computer.png",80,48,0.2f,Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(normalAnimation);
        this.electricityFlow = false;
    }

    public int add(int num1, int num2){
        if(!this.electricityFlow) return 0;
        return num1 + num2;
    }

    public int sub(int num1,int num2){
        if(!this.electricityFlow) return 0;
        return num1 - num2;
    }

    public float add(float num1,float num2){
        if(!this.electricityFlow) return 0;
        return num1 + num2;
    }

    public float sub(float num1, float num2){
        if(!this.electricityFlow) return 0;
        return num1 - num2;
    }

    @Override
    public void setPowered(boolean power) {
        this.electricityFlow = power;
        if(power){
            getAnimation().setTint(Color.WHITE);
        }
        else getAnimation().setTint(Color.GRAY);
    }
}
