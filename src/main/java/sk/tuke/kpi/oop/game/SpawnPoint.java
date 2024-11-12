package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class SpawnPoint extends AbstractActor {
    private int countOfAliens;
    private Disposable spawnerBehavior;
    public SpawnPoint(int countOfAliens){
        this.countOfAliens = countOfAliens;
        Animation animation = new Animation("sprites/spawn.png",32,32);
        setAnimation(animation);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        setSpawnerBehavior();
    }

    public void spawnAlien(){
        if(countOfAliens <= 0) {
            this.spawnerBehavior.dispose();
            return;
        }
        Scene scene = getScene();
        if(scene == null) return;
        Ripley ripley = scene.getFirstActorByType(Ripley.class);
        if(ripley == null) return;

        Rectangle2D.Float rect = new Rectangle2D.Float(ripley.getPosX() - 16,ripley.getPosY() - 16,ripley.getWidth(),ripley.getHeight());
        Ellipse2D.Float ellipse = new Ellipse2D.Float(this.getPosX() - 50,this.getPosY() - 50,100,100);
        if(ellipse.intersects(rect)){
            scene.addActor(new Alien(100, new RandomlyMoving()),this.getPosX() ,this.getPosY());
            this.countOfAliens--;
            resetBehavior();
        }

       /* int x1_x2 = (ripley.getPosX() + 16) - (this.getPosX() + 16);
        int y1_y2 = (ripley.getPosY() + 16) - (this.getPosY() + 16);
        double length = Math.sqrt(Math.pow(x1_x2,2) + Math.pow(y1_y2,2));
        if(length <= 50){
            scene.addActor(new Alien(100, new RandomlyMoving()),this.getPosX() + 16,this.getPosY() + 16);
            this.countOfAliens--;
        }*/

    }

    public void resetBehavior(){
        this.spawnerBehavior.dispose();
        new ActionSequence<>(
            new Wait<>(3),
            new Invoke<>(this::setSpawnerBehavior)
        ).scheduleFor(this);
    }

    public void setSpawnerBehavior(){
        this.spawnerBehavior = new Loop<>(new ActionSequence<>(
            new Invoke<>(this::spawnAlien)
        )).scheduleFor(this);
    }
}
