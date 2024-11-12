package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.oop.game.Locker;
import sk.tuke.kpi.oop.game.Ventilator;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.items.*;
import sk.tuke.kpi.oop.game.openables.Door;
import sk.tuke.kpi.oop.game.openables.LockedDoor;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class MissionImpossible implements SceneListener {
    private Ripley ripley;
    private Timer timer;
    private boolean isRepairedVentilator;
    public static class Factory implements ActorFactory{

        @Override
        public @Nullable Actor create(@Nullable String type, @Nullable String name) {
            if(name == null) return null;
            switch (name){
                case "energy":
                    return new Energy();
                case "ellen":
                    return new Ripley();
                case "front door":
                    return new LockedDoor(name,Door.Orientation.VERTICAL);
                case "access card":
                    return new AccessCard();
                case "locker":
                    return new Locker();
                case "ventilator":
                    return new Ventilator();
                default:
                    return null;
            }
        }
    }
    @Override
    public void sceneInitialized(@NotNull Scene scene) {
        SceneListener.super.sceneInitialized(scene);

        ripley = scene.getFirstActorByType(Ripley.class);
        MovableController controller = new MovableController(ripley);
        Disposable controlerDisposable = scene.getInput().registerListener(controller);

        KeeperController keeperController = new KeeperController(ripley);
        Disposable keepperControlerDisposable = scene.getInput().registerListener(keeperController);

        FireExtinguisher fireExtinguisher = new FireExtinguisher();
        Wrench wrench = new Wrench();
        Hammer hammer = new Hammer();
        ripley.getBackpack().add(fireExtinguisher);
        ripley.getBackpack().add(wrench);
        ripley.getBackpack().add(hammer);

        scene.getGame().pushActorContainer(ripley.getBackpack());

        scene.follow(ripley);

        timer = new Timer(); //GAS DISTRIBUTION
        isRepairedVentilator = false;

        scene.getMessageBus().subscribe(
            Door.DOOR_OPENED,
            new Consumer<>() {
                @Override
                public void accept(Door door) {
                    if(isRepairedVentilator) return;
                    if(timer == null) {
                        timer = new Timer();
                    }
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            ripley.getHealth().setValue(ripley.getHealth().getValue() - 1);
                        }
                    }, 400, 400);
                }
            });

        scene.getMessageBus().subscribe(
            Door.DOOR_CLOSED,
            new Consumer<>() {
                @Override
                public void accept(Door door) {
                    if(timer == null) return;
                    timer.cancel();
                    timer = null;
                }
            });

        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, new Consumer<>() {
            @Override
            public void accept(Ripley ripley) {
                controlerDisposable.dispose();
                keepperControlerDisposable.dispose();
            }
        });

        scene.getMessageBus().subscribe(Ventilator.VENTILATOR_REPAIRED, new Consumer<>() {
            @Override
            public void accept(Ventilator ventilator) {
                if(timer == null) return;
                timer.cancel();
                timer = null;
                isRepairedVentilator = true;
            }
        });
    }

    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        SceneListener.super.sceneUpdating(scene);
        ripley.showRipleyState();
    }
}
