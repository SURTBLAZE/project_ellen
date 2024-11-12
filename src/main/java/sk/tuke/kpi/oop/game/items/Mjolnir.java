package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.graphics.Animation;

public class Mjolnir extends Hammer{
    public Mjolnir(){
        Animation animation = new Animation("sprites/hammer.png");
        setAnimation(animation);
        this.setRemainingUses(4);
    }
}
