package sk.tuke.kpi.oop.game.actions;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.items.AccessCard;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.openables.LockedDoor;

public class Use<A extends Actor> extends AbstractAction<A> {
    private Usable<A> usable;
    public Use(Usable<A> usableActor){
        this.usable = usableActor;
    }

    @Override
    public void execute(float deltaTime) {
        if(!isDone()){
            setDone(true);
            usable.useWith(getActor());
        }
    }

    public Disposable scheduleForIntersectingWith(@NotNull Actor mediatingActor) {
        Scene scene = mediatingActor.getScene();
        if (scene == null) return null;
        Class<A> usingActorClass = usable.getUsingActorClass();  // `usable` je spominana clenska premenna

        for (Actor actor : scene) {
            if (mediatingActor.intersects(actor) && usingActorClass.isInstance(actor) ){
                if((usable instanceof AccessCard) && !(actor instanceof LockedDoor)) continue;
                return this.scheduleFor(usingActorClass.cast(actor));  // naplanovanie akcie v pripade, ze sa nasiel vhodny akter
            }
        }
        return null;
    }
}
