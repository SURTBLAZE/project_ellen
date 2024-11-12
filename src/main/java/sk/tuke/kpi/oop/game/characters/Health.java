package sk.tuke.kpi.oop.game.characters;


import java.util.ArrayList;
import java.util.List;

public class Health {
    private int health_points;
    private int maxhealth;

    private List<FatigueEffect> efects;
    private boolean isApplied = false;

    @FunctionalInterface
    public interface FatigueEffect {
        void apply();
    }

    public void onFatigued(FatigueEffect effect) {
        if(effect == null) return;
        this.efects.add(effect);
    }

    public Health(int defaulthealth,int maxhealth){
        this.health_points = defaulthealth;
        this.maxhealth = maxhealth;
        this.efects = new ArrayList<>();
    }

    public Health(int defaulthealth){
        this.health_points = defaulthealth;
        this.maxhealth = defaulthealth;
        this.efects = new ArrayList<>();
    }

    public void refill(int amount){
        this.health_points += amount;
        if(this.health_points > this.maxhealth){
            this.health_points = this.maxhealth;
        }
    }

    public void restore(){
        this.health_points = this.maxhealth;
    }

    public void drain(int amount){
        this.health_points -= amount;
        if(this.health_points <= 0){
            this.health_points = 0;
            if(isApplied) return;
            for(FatigueEffect efect : efects){
                efect.apply();
            }
            isApplied = true;
        }
    }

    public void exhaust(){
        this.drain(this.maxhealth);
    }

    public void setValue(int value){
        if(value <= maxhealth) {
            this.health_points = value;
        }
    }

    public int getValue() {
        return health_points;
    }
}
