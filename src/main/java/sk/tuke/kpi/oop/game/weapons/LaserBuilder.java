package sk.tuke.kpi.oop.game.weapons;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class LaserBuilder implements Builder{
    private Animation ammoAnimation;
    private int ammoSpeed;

    @Override
    public void setAmmoAnimation(Animation animation) {
        this.ammoAnimation = animation;
    }

    @Override
    public void setAmmoSpeed(int speed) {
        this.ammoSpeed = speed;
    }

    public Laser getResult(){
        return new Laser(ammoAnimation,ammoSpeed);
    }
}
