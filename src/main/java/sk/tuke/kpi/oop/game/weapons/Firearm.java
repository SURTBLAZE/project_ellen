package sk.tuke.kpi.oop.game.weapons;

public abstract class Firearm {

    private int amunition;
    private int maxAmunition;
    public Firearm(int amunition, int maxAmunition){
        this.amunition = amunition;
        this.maxAmunition = maxAmunition;
    }

    public Firearm(int amunition){
        this.amunition = amunition;
        this.maxAmunition = amunition;
    }

    public abstract Firearm clone();

    public int getAmmo(){
        return this.amunition;
    }
    public int getMaxAmunition(){return this.maxAmunition;}

    public void setAmmo(int amunition){
        if(amunition < 0) return;
        this.amunition = amunition;
    }

    public void reload(int newAmmo){
        this.amunition += newAmmo;
        if(this.amunition > this.maxAmunition){
            this.amunition = this.maxAmunition;
        }
    }

    public Fireable fire(){
        if(amunition == 0) return null;
        amunition--;
        return createBullet();
    }

    protected abstract Fireable createBullet();
}
