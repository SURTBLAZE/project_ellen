package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Collectible;

import java.util.List;
import java.util.stream.Collectors;

public class Take<A extends Keeper> extends AbstractAction<A> {
    //private Keeper actor;
    /*public Take(Keeper actor){
        this.actor = actor;
    }*/
    @Override
    public void execute(float deltaTime) {
        if(getActor() == null || getActor().getScene() == null){
            setDone(true);
            return;
        }
        if(!isDone()){
            if(getActor().getScene() == null) return;
            List<Actor> items = getActor().getScene().getActors().stream().filter(i -> getActor().intersects(i)).collect(Collectors.toList());
            for(Actor item: items){
                if(item instanceof Collectible) {
                    try {
                        getActor().getBackpack().add((Collectible) item);
                    }catch (Exception ex){
                        getActor().getScene().getGame().getOverlay().drawText(ex.getMessage(), 270, 50).showFor(2);
                        return;
                    }
                    getActor().getScene().removeActor(item);
                    break;
                }
            }
            setDone(true);
        }
    }
}
