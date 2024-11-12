package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.SceneListener;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.items.FireExtinguisher;
import sk.tuke.kpi.oop.game.items.Hammer;
import sk.tuke.kpi.oop.game.items.Wrench;

public class FirstSteps implements SceneListener {
    private Ripley ripley;
    @Override
    public void sceneInitialized(@NotNull Scene scene) {
        SceneListener.super.sceneInitialized(scene);

        ripley = new Ripley();
        scene.addActor(ripley,0,0);

        MovableController controller = new MovableController(ripley);
        scene.getInput().registerListener(controller);
        KeeperController keeperController = new KeeperController(ripley);
        scene.getInput().registerListener(keeperController);

        FireExtinguisher fireExtinguisher = new FireExtinguisher();
        Wrench wrench = new Wrench();
        Hammer hammer = new Hammer();
        ripley.getBackpack().add(fireExtinguisher);
        ripley.getBackpack().add(wrench);
        ripley.getBackpack().add(hammer);
        Hammer hammer2 = new Hammer();
        scene.addActor(hammer2,0,0);

        scene.getGame().pushActorContainer(ripley.getBackpack());
    }

    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        SceneListener.super.sceneUpdating(scene);

        int windowHeight = scene.getGame().getWindowSetup().getHeight();
        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;
        scene.getGame().getOverlay().drawText("Energy: " + ripley.getHealth().getValue(),110,yTextPos);
    }
}
