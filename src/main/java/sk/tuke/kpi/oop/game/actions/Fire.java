package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.weapons.Fireable;

public class Fire<A extends Armed> extends AbstractAction<A> {
    @Override
    public void execute(float deltaTime) {
        if(isDone()) return;
        if(getActor() == null){
            setDone(true);
            return;
        }
        //get a bullet of the weapon
        Fireable fireable = getActor().getFirearm().fire();
        if(fireable == null) return;
        //add to scene
        if(getActor().getScene() == null) return;
        int pomX = Direction.fromAngle(getActor().getAnimation().getRotation()).getDx();
        int pomY = Direction.fromAngle(getActor().getAnimation().getRotation()).getDy();

        getActor().getScene().addActor(fireable,getActor().getPosX() + 8 + pomX*24,getActor().getPosY() + 8 + pomY*24);
        new Move<Fireable>(Direction.fromAngle(getActor().getAnimation().getRotation()),1000000).scheduleFor(fireable);
        setDone(true);
    }
}
