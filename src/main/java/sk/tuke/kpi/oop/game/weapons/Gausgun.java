package sk.tuke.kpi.oop.game.weapons;

import sk.tuke.kpi.gamelib.graphics.Animation;

public class Gausgun extends Firearm{
    public Gausgun(int amunition,int maxAmunition){
        super(amunition,maxAmunition);
    }

    public Gausgun(Gausgun target){
        super(target.getAmmo(),target.getMaxAmunition());
    }

    @Override
    public Firearm clone() {
        return new Gausgun(this);
    }

    @Override
    protected Fireable createBullet() {
        Director director = new Director();
        LaserBuilder laserBuilder = new LaserBuilder();
        director.constructBlueLaser(laserBuilder);
        return laserBuilder.getResult();
    }
}
