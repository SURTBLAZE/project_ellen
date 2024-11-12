package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;

public class SmartCooler extends Cooler {

    public SmartCooler(Reactor reactor){
        super(reactor);
    }

    private void coolReactor(){
        if(this.getReactor() == null) return;

        if(!isOn() && this.getReactor().getTemperature() > 2500) this.turnOn();
        else if (isOn() && this.getReactor().getTemperature() < 1500) this.turnOff();

        if(isOn()){
            this.getReactor().decreaseTemperature(1);
        }
    }

    @Override
    public void addedToScene(Scene scene){
        if(scene == null) return;

        super.addedToScene(scene);
        new Loop<>(new Invoke<>(SmartCooler::coolReactor)).scheduleFor(this);
    }
}
