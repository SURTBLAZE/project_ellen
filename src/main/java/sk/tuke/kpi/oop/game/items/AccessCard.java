package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.openables.LockedDoor;

public class AccessCard extends AbstractActor implements Usable<Actor>,Collectible {
    private String name;
    public AccessCard(){
        Animation animation = new Animation("sprites/key.png",16,16);
        setAnimation(animation);
        this.name = "default";
    }

    public AccessCard(String name){
        Animation animation = new Animation("sprites/key.png",16,16);
        setAnimation(animation);
        this.name = name;
    }
    @Override
    public void useWith(Actor actor) {
        if(actor instanceof LockedDoor){
            if(this.name.equals(actor.getName())) {
                ((LockedDoor) actor).unlock();
            }
        }
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }

    @Override
    public String getName(){
        return this.name;
    }
}
