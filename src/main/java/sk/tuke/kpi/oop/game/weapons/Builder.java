package sk.tuke.kpi.oop.game.weapons;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.graphics.Animation;

public interface Builder {
    void setAmmoAnimation(Animation animation);
    void setAmmoSpeed(int speed);
}
