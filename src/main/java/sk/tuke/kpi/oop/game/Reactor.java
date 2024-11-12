package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.actions.PerpetualReactorHeating;
import sk.tuke.kpi.oop.game.openables.Door;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Reactor extends AbstractActor implements Switchable,Repairable{
    private int temperature;
    private int damage;
    private Animation normalAnimation;
    private Animation offAnimation;
    private Animation overheatAnimation_005; //005 - means Frame Duration
    private Animation overheatAnimation_0035;
    private Animation overheatAnimation_0025;
    private Animation brokenAnimation;
    private Animation extinguishedAnimation;
    private boolean state;

    public static final Topic<Reactor> REACTOR_TURNED_ON = Topic.create("reactor turned on", Reactor.class);
    public static final Topic<Reactor> REACTOR_TURNED_OFF = Topic.create("reactor turned off", Reactor.class);

    private Set<EnergyConsumer> devices;

    public Reactor(){
        temperature = 0;
        damage = 0;
        state = false;
        offAnimation = new Animation("sprites/reactor.png");
        overheatAnimation_005 = new Animation("sprites/reactor_hot.png",80,80,0.05f,Animation.PlayMode.LOOP_PINGPONG);
        overheatAnimation_0035 = new Animation("sprites/reactor_hot.png",80,80,0.035f,Animation.PlayMode.LOOP_PINGPONG);
        overheatAnimation_0025 = new Animation("sprites/reactor_hot.png",80,80,0.025f,Animation.PlayMode.LOOP_PINGPONG);
        brokenAnimation = new Animation("sprites/reactor_broken.png",80,80,0.1f,Animation.PlayMode.LOOP_PINGPONG);
        normalAnimation = new Animation("sprites/reactor_on.png",80,80,0.1f,Animation.PlayMode.LOOP_PINGPONG);
        extinguishedAnimation = new Animation("sprites/reactor_extinguished.png");
        setAnimation(offAnimation);
        devices = new HashSet<>();
    }

    public void increaseTemperature(int increment){
        if(increment < 0 || !this.state) return;
        if(this.damage >= 33 && this.damage <= 66){
            this.temperature += (int)Math.ceil(increment * 1.5);
        }
        else if(this.damage >= 66) {
            this.temperature += increment * 2;
        }
        else{
            this.temperature += increment;
        }

        //Change damage
        if(this.temperature > 2000){
            int new_damage = (int)((this.temperature - 2000) / 40);
            if(new_damage > this.damage) this.damage = new_damage;
            if(this.damage > 100) this.damage = 100;
            updateAnimation();
        }
    }

    public void decreaseTemperature(int decrement){
        if(decrement < 0 || !this.state) return;
        if(this.damage >= 50){
            this.temperature -= (int)(decrement*0.5);
        }
        else{
            this.temperature -= decrement;
        }

        if(this.temperature < 0) this.temperature = 0;

        updateAnimation();
    }

    private void updateAnimation(){
        if(this.temperature >= 6000){
            this.turnOff();
            setAnimation(brokenAnimation);
        }
        else if(this.temperature > 4000) {
            if(this.damage > 85){
                setAnimation(overheatAnimation_0025);
            }
            else if(this.damage > 65) {
                setAnimation(overheatAnimation_0035);
            }
            else{
                setAnimation(overheatAnimation_005);
            }
        }
        else{
            setAnimation(normalAnimation);
        }
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void turnOn(){
        if (damage != 100) {
            state = true;
            updateAnimation();
            Iterator<EnergyConsumer> iterator = devices.iterator();
            while (iterator.hasNext()) {
                EnergyConsumer device = iterator.next();
                if (device != null) device.setPowered(true);
            }
            if(getScene() == null) return;
            getScene().getMessageBus().publish(REACTOR_TURNED_ON,this);
        }
    }

    public void turnOff() {
        if (damage != 100) {
            temperature = 0;
            setAnimation(offAnimation);
        }
        state = false;
        Iterator<EnergyConsumer> iterator = devices.iterator();
        while (iterator.hasNext()) {
            EnergyConsumer device = iterator.next();
            if (device != null) device.setPowered(false);
        }
        if(getScene() == null) return;
        getScene().getMessageBus().publish(REACTOR_TURNED_OFF,this);
    }

    public int getTemperature() {
        return temperature;
    }

    public int getDamage() {
        return damage;
    }

    public boolean repair(){  //For Hammer
        if(damage > 0 && damage < 100){
            damage -= 50;
            if(damage < 0) damage = 0;
            int new_temp = 2000 + damage * 40;
            if(new_temp < this.temperature) this.temperature = new_temp;
            updateAnimation();
            return true;
        }
        else return false;
    }

    public boolean extinguish(){ //For FireExtinguisher
        if(damage != 100) return false;

        this.temperature -= 4000;
        if(temperature < 0) temperature = 0;
        setAnimation(extinguishedAnimation);
        return true;
    }

    public boolean isOn(){
        return state;
    }

    public void addDevice(EnergyConsumer device){
        if(device == null || this.devices.contains(device)) return;
        this.devices.add(device);
        if(damage == 100 || !isOn()) return;
        device.setPowered(true);
    }

    public void removeDevice(EnergyConsumer device){
        if(device == null || !this.devices.contains(device)) return;
        device.setPowered(false);
        this.devices.remove(device);
    }

    @Override
    public void addedToScene(Scene scene){
        if(scene == null) return;

        super.addedToScene(scene);
        scene.scheduleAction(new PerpetualReactorHeating(60),this);
    }

}
