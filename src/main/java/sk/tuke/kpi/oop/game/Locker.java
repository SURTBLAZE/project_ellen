package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.items.Hammer;
import sk.tuke.kpi.oop.game.items.Usable;

public class Locker extends AbstractActor implements Usable<Actor> {
    private boolean state;
    public Locker(){
        Animation animation = new Animation("sprites/locker.png",16,16);
        setAnimation(animation);
        this.state = false;
    }
    @Override
    public void useWith(Actor actor) {
        if(isOpen()) return;
        Hammer hammer = new Hammer();
        getScene().addActor(hammer,this.getPosX(),this.getPosY());
        this.state = true;
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }

    public boolean isOpen(){
        return this.state;
    }
}
