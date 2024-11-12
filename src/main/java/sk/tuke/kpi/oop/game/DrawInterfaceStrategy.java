package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.oop.game.characters.MotherAlien;
import sk.tuke.kpi.oop.game.characters.Ripley;

public class DrawInterfaceStrategy extends AbstractActor implements DrawStrategy{
    @Override
    public void draw(Scene scene) {
        Ripley ripley = scene.getFirstActorByType(Ripley.class);
        int windowHeight = scene.getGame().getWindowSetup().getHeight();
        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;
        scene.getGame().getOverlay().drawText("Energy: " + ripley.getHealth().getValue(),110,yTextPos);
        if(scene.getFirstActorByType(MotherAlien.class) == null){
            scene.getGame().getOverlay().drawText("Energy mother: killed",110,yTextPos - 30);
            return;
        }
        scene.getGame().getOverlay().drawText("Energy mother: " + scene.getFirstActorByType(MotherAlien.class).getHealth().getValue(),110,yTextPos - 30);
    }
}
