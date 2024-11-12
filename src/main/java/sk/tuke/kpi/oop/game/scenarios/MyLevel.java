package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.oop.game.*;
import sk.tuke.kpi.oop.game.behaviours.Observing;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.*;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.ShooterController;
import sk.tuke.kpi.oop.game.items.*;
import sk.tuke.kpi.oop.game.openables.Door;
import sk.tuke.kpi.oop.game.openables.LockedDoor;
import sk.tuke.kpi.oop.game.weapons.Bazooka;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class MyLevel implements SceneListener {
    private Ripley ripley;

    private Timer timer; //for gas atack
    private boolean isRepairedVentilator; //to check ventilator state
    public static class Factory implements ActorFactory {
        @Override
        public @Nullable Actor create(@Nullable String type, @Nullable String name) {
            if (name == null) return null;
            switch (name){
                case "energy":
                    return new Energy();
                case "ellen":
                    return new Ripley();
                case "solder":
                    if(type.equals("east")) {
                        return new Solder(Direction.EAST);
                    }
                    else if (type.equals("north")){
                        return new Solder(Direction.NORTH);
                    }
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
                case "bug":
                    if(type.equals("storage door")){
                        return new Bug(type);
                    }
                    return new Bug();
                case "opened door":
                    if(type.equals("vertical")){
                        return new Door(name,Door.Orientation.VERTICAL);
                    }
                    else if(type.equals("horizontal")){
                        return new Door(name,Door.Orientation.HORIZONTAL);
                    }
                case "storage door":
                    return new LockedDoor(name,Door.Orientation.VERTICAL);
                case "ventilators door":
                    return new LockedDoor(name,Door.Orientation.HORIZONTAL);
                case "boss door":
                    return new LockedDoor(name,Door.Orientation.VERTICAL);
                case "access card":
                    return new AccessCard(type);
                case "ammo":
                    return new Ammo();
                case "locker":
                    return new Locker();
                case "ventilator":
                    if(type.equals("broken")){
                        return new Ventilator(true);
                    }
                    return new Ventilator();
                case "reactor":
                    return new Reactor();
                case "cooler":
                    return new Cooler(null);
                case "button":
                    return new Button(type);
                case "teleport":
                    return new Teleport(null);
                case "alien mother":
                    if(type.equals("waiting2")){
                        return new MotherAlien(200, new Observing<>(
                            Door.DOOR_OPENED,
                            door -> door.getName().equals("back door"),
                            new RandomlyMoving()
                        ));
                    }
                    return new MotherAlien(10000, new RandomlyMoving());
                case "spawner":
                    return new SpawnPoint(5);
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

        scene.getGame().pushActorContainer(ripley.getBackpack());

        //Inicializacia teleportov
        List<Teleport> teleports = new ArrayList<>();
        for(Actor actor : scene.getActors()){
            if(actor instanceof Teleport){
                teleports.add((Teleport) actor);
            }
        }
        teleports.get(0).setDestination(teleports.get(1));

        //Inicializacia tlacidla
        Reactor reactor = scene.getFirstActorByType(Reactor.class);
        Cooler cooler = scene.getFirstActorByType(Cooler.class);
        for(Actor actor : scene.getActors()) {
            if(actor instanceof Button){
                if(actor.getName().equals("reactor button")){
                    ((Button) actor).setSwitchable(reactor);
                }
                else if(actor.getName().equals("cooler button")){
                    ((Button) actor).setSwitchable(cooler);
                }
            }
        }
        if(cooler != null) {
            cooler.setReactor(reactor);
        }

        //Observer for Mother Alien
        scene.getMessageBus().subscribe(
            MotherAlien.MOTHER_ALIEN_DEAD,
            new Consumer<>() {
                @Override
                public void accept(MotherAlien motherAlien) {
                    if(motherAlien.getScene() == null) return;
                    motherAlien.getScene().removeActor(motherAlien);
                    motherAlien.getScene().getGame().getOverlay().drawText("Mother of Alien is dead.\nThe space station is saved.\nEND OF GAME.", 80, 100).showFor(400);
                }
            }
        );

        //Observer for Reactor
        scene.getMessageBus().subscribe(
            Reactor.REACTOR_TURNED_ON,
            new Consumer<>() {
                @Override
                public void accept(Reactor reactor) {
                    for(Actor actor : scene.getActors()){
                        if(actor instanceof LockedDoor && actor.getName().equals("boss door")){
                            ((LockedDoor) actor).unlock();
                        }
                    }
                }
            }
        );

        scene.getMessageBus().subscribe(
            Reactor.REACTOR_TURNED_OFF,
            new Consumer<>() {
                @Override
                public void accept(Reactor reactor) {
                    for(Actor actor : scene.getActors()){
                        if(actor instanceof LockedDoor && actor.getName().equals("boss door")){
                            ((LockedDoor) actor).lock();
                        }
                    }
                }
            }
        );

        //Observer for boss door
        timer = new Timer(); //GAS DISTRIBUTION
        isRepairedVentilator = false;

        scene.getMessageBus().subscribe(
            Door.DOOR_OPENED,
            new Consumer<>() {
                @Override
                public void accept(Door door) {
                    if(isRepairedVentilator) return;
                    if(!door.getName().equals("boss door")) return; //only for boss door
                    if(timer == null) {
                        timer = new Timer();
                    }
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            ripley.getHealth().drain(1);
                        }
                    }, 800, 800);
                }
            });

        scene.getMessageBus().subscribe(
            Door.DOOR_CLOSED,
            new Consumer<>() {
                @Override
                public void accept(Door door) {
                    if(timer == null || !door.getName().equals("boss door")) return;
                    timer.cancel();
                    timer = null;
                }
            });

        //Observer for ventilator
        scene.getMessageBus().subscribe(Ventilator.VENTILATOR_REPAIRED, new Consumer<>() {
            @Override
            public void accept(Ventilator ventilator) {
                if(timer == null) return;
                timer.cancel();
                timer = null;
                isRepairedVentilator = true;
                scene.getFirstActorByType(Ripley.class).setWeapon(Bazooka.getBazookaInstance(20,20));
            }
        });

        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, new Consumer<>() {
            @Override
            public void accept(Ripley ripley) {
                controlerDisposable.dispose();
                keepperControlerDisposable.dispose();
                shooterControllerDisposable.dispose();
            }
        });

        scene.follow(ripley);
    }

    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        SceneListener.super.sceneUpdating(scene);
        DrawStrategy drawStrategy = new DrawInterfaceStrategy();
        DrawOrder drawOrder = new DrawOrder();
        drawOrder.proccessOrder(drawStrategy,scene);
    }
}
