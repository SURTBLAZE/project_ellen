package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.oop.game.Locker;
import sk.tuke.kpi.oop.game.SpawnPoint;
import sk.tuke.kpi.oop.game.Ventilator;
import sk.tuke.kpi.oop.game.behaviours.Observing;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.MotherAlien;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.ShooterController;
import sk.tuke.kpi.oop.game.items.*;
import sk.tuke.kpi.oop.game.openables.Door;

public class EscapeRoom implements SceneListener {
    private Ripley ripley;
    public static class Factory implements ActorFactory {

        @Override
        public @Nullable Actor create(@Nullable String type, @Nullable String name) {
            if (name == null) return null;
            switch (name){
                case "energy":
                    return new Energy();
                case "ellen":
                    return new Ripley();
                case "alien":
                    if(type.equals("running")) {
                        return new Alien();
                    }
                    else if(type.equals("waiting1")) {
                        return new Alien(100, new Observing<>(
                            Door.DOOR_OPENED,
                            door -> door.getName().equals("front door"),
                            new RandomlyMoving()
                        ));
                    }
                    else if(type.equals("waiting2")){
                        return new Alien(100, new Observing<>(
                            Door.DOOR_OPENED,
                            door -> door.getName().equals("back door"),
                            new RandomlyMoving()
                        ));
                    }
                case "front door":
                    return new Door(name,Door.Orientation.VERTICAL);
                case "back door":
                    return new Door(name,Door.Orientation.HORIZONTAL);
                case "exit door":
                    return new Door(name,Door.Orientation.VERTICAL);
                case "access card":
                    return new AccessCard();
                case "ammo":
                    return new Ammo();
                case "locker":
                    return new Locker();
                case "ventilator":
                    return new Ventilator();
                case "alien mother":
                    if(type.equals("waiting2")){
                        return new MotherAlien(200, new Observing<>(
                            Door.DOOR_OPENED,
                            door -> door.getName().equals("back door"),
                            new RandomlyMoving()
                        ));
                    }
                    return new MotherAlien(200, null);
                case "spawner":
                    return new SpawnPoint(10000);
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

        ShooterController shooterController = new ShooterController(ripley);
        Disposable shooterControllerDisposable = scene.getInput().registerListener(shooterController);

        FireExtinguisher fireExtinguisher = new FireExtinguisher();
        Wrench wrench = new Wrench();
        Hammer hammer = new Hammer();
        ripley.getBackpack().add(fireExtinguisher);
        ripley.getBackpack().add(wrench);
        ripley.getBackpack().add(hammer);
        scene.getGame().pushActorContainer(ripley.getBackpack());

        scene.follow(ripley);
    }

    @Override
    public void sceneCreated(@NotNull Scene scene) {
        SceneListener.super.sceneCreated(scene);

       /* scene.getMessageBus().subscribe(World.ACTOR_ADDED_TOPIC, (actor) -> {
           if(actor instanceof Alien){
               Alien alien = (Alien) actor;
               new Loop<>(new Invoke<>(() -> {
                   int oldX = alien.getPosX(),oldY = alien.getPosY();
                   int newX = oldX + (int)(Math.random() * 3) - 1;
                   int newY = oldY + (int)(Math.random() * 3) - 1;
                   alien.setPosition(newX,newY);
                   if(alien.getScene().getMap().intersectsWithWall(alien)){
                       alien.setPosition(oldX, oldY);
                   }
               })).scheduleFor(alien);
           }
        });*/
    }

    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        SceneListener.super.sceneUpdating(scene);

        int windowHeight = scene.getGame().getWindowSetup().getHeight();
        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;
        scene.getGame().getOverlay().drawText("Energy: " + ripley.getHealth().getValue(),110,yTextPos);
    }
}
