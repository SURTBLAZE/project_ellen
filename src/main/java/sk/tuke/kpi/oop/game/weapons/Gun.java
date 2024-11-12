package sk.tuke.kpi.oop.game.weapons;

public class Gun extends Firearm{
    public Gun(int amunition,int maxAmunition){
        super(amunition,maxAmunition);
    }
    public Gun(Gun target){
        super(target.getAmmo(),target.getMaxAmunition());
    }
    @Override
    public Firearm clone() {
        return new Gun(this);
    }

    @Override
    protected Fireable createBullet() {
        return new Bullet();
    }
}
