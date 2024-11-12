package sk.tuke.kpi.oop.game.weapons;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Director extends AbstractActor {
    public void constructBlueLaser(Builder builder){
        builder.setAmmoAnimation(new Animation("sprites/laser_beam.png",16,16));
        builder.setAmmoSpeed(6);
    }
}
