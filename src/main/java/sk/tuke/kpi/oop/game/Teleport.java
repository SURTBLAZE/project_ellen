package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.Player;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.awt.geom.Rectangle2D;

public class Teleport extends AbstractActor {
    private Teleport destination;

    private boolean avaible;
    public Teleport(Teleport nextTeleport){
        this.destination = nextTeleport;
        Animation animation = new Animation("sprites/lift.png",48,48);
        setAnimation(animation);
        this.avaible = true;
    }

    public Teleport getDestination() {
        return destination;
    }

    public void teleportPlayer(Alive player){
        if(player == null || !this.avaible) return;
       // if(this.destination == null) return;

        this.setAvaible(false);
        player.setPosition(this.getPosX() + 8,this.getPosY() + 8);
        //destination.teleportPlayer(player);
    }

    public void setDestination(Teleport destinationTeleport) {
        if(destinationTeleport == this) return;
        this.destination = destinationTeleport;
     }

    public void setAvaible(boolean avaible) {
        this.avaible = avaible;
    }

    public void checkTeleport(){
        Scene scene = getScene();
        if(scene == null) return;
        //Ripley player = scene.getFirstActorByType(Ripley.class);
        //if(player == null) return;
        for (Actor actor : scene.getActors()) {
            if(!(actor instanceof Alive)) continue;
            Rectangle2D.Float telerect = new Rectangle2D.Float(this.getPosX(), this.getPosY(), 48, 48);
            Rectangle2D.Float rect = new Rectangle2D.Float(actor.getPosX() + 16, actor.getPosY() + 16, 1, 1);

            if (telerect.intersects(rect) && this.avaible && this.destination != null) {
                destination.teleportPlayer((Alive) actor);
            } else if (!telerect.intersects(rect) && !this.avaible) {
                this.avaible = true;
            }
        }
    }

    @Override
    public void addedToScene(Scene scene) {
        if (scene == null) return;

        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::checkTeleport)).scheduleFor(this);
    }
}
