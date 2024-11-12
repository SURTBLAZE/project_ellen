package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.Scenario;
import sk.tuke.kpi.oop.game.Teleport;

public class TrainingGameplay extends Scenario {

    @Override
    public void setupPlay(@NotNull Scene scene) {
        Actor player = scene.getFirstActorByName("Player");

       /* Reactor reactor = new Reactor();
        scene.addActor(reactor,64,64);
        reactor.turnOn();

        Cooler cooler = new Cooler(reactor);
        scene.addActor(cooler, 160,64);

        new ActionSequence<>(
            new Wait<>(5),
            new Invoke<>(cooler::turnOn),
            new Wait<>(5),
            new Invoke<>(cooler::turnOff)
        ).scheduleFor(cooler);

        Hammer hammer = new Hammer();
        scene.addActor(hammer,32,32);
        System.out.println(player.getHeight() + ":" + player.getWidth());


        */
        Teleport teleport1 = new Teleport(null);
        scene.addActor(teleport1,80,112);
        Teleport teleport2 = new Teleport(null);
        scene.addActor(teleport2,58,331);
        Teleport teleport3 = new Teleport(null);
        scene.addActor(teleport3,260,331);
        teleport1.setDestination(teleport2);
        teleport2.setDestination(teleport3);

        /*ChainBomb bomb1 = new ChainBomb(2);
        scene.addActor(bomb1,150,150);
        ChainBomb bomb2 = new ChainBomb(2);
        scene.addActor(bomb2,150,93);
        ChainBomb bomb3 = new ChainBomb(2);
        scene.addActor(bomb3,200,95);
        ChainBomb bomb4 = new ChainBomb(2);
        scene.addActor(bomb4,125,70);
        ChainBomb bomb5 = new ChainBomb(2);
        scene.addActor(bomb5,200,50);
        bomb1.activate();*/


       // Helicopter helicopter = new Helicopter();
      //  scene.addActor(helicopter,20,20);
        /*new Invoke<>(new Runnable() {  // zapiseme priamo implementaciu rozhrania
            @Override
            public void run() {
                reactor.repairWith(hammer);
            }
        }); */
     //   new Loop<>(new Invoke<>(helicopter::searchAndDestroy)).scheduleFor(helicopter);
       // new Invoke<>(() -> reactor.repairWith(hammer));

        /*new When<>(
            () -> {
                return !player.intersects(helicopter);
            },
            new Invoke<>(() -> {
                helicopter.searchAndDestroy();
            })
        ).scheduleFor(reactor);*/

       /* new When<>(
            () -> {
                return reactor.getTemperature() >= 3000;
            },
            new Invoke<>(() -> {
                hammer.useWith(reactor);
            })
        ).scheduleFor(reactor);*/
    }
}
