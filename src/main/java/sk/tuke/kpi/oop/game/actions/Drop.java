package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Collectible;

public class Drop <A extends Keeper> extends AbstractAction<A> {
    //private Keeper actor;
    /*public Drop(Keeper actor){
        this.actor = actor;
    }*/

    @Override
    public void execute(float deltaTime) {
        if(getActor() == null || getActor().getScene() == null || getActor().getBackpack() == null){
            setDone(true);
            return;
        }
        if(!isDone()){
            Collectible item = getActor().getBackpack().peek();
            if(item == null) {
                setDone(true);
                return;
            }
            getActor().getBackpack().remove(item);
            getActor().getScene().addActor(item,getActor().getPosX(),getActor().getPosY());
        }
        setDone(true);
    }
}
