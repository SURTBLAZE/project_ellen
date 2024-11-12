package sk.tuke.kpi.oop.game.weapons;

public class Bazooka extends Firearm{
    private static Bazooka bazookaInstance;
    private Bazooka(int amunition,int maxAmunition){
        super(amunition,maxAmunition);
    }
    private Bazooka(Bazooka target){
        super(target.getAmmo(),target.getMaxAmunition());
    }

    public static Bazooka getBazookaInstance(int amunition,int maxAmunition){
        if(bazookaInstance == null){
            bazookaInstance = new Bazooka(amunition,maxAmunition);
        }
        return bazookaInstance;
    }

    @Override
    public Firearm clone() {
        return new Bazooka(this);
    }

    @Override
    protected Fireable createBullet() {
        return new Rocket();
    }
}
