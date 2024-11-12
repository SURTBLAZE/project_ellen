package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.Player;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;


public class Helicopter extends AbstractActor {

    public Helicopter(){
        Animation animation = new Animation("sprites/heli.png",64,64,0.01f,Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
    }

    private void searchingAndDestroying(){
        Scene scene = getScene(); //get scene
        Actor player = scene.getFirstActorByName("Player"); //get player
        if(player == null) return;


        if(this.intersects(player)) {
            ((Player) player).setEnergy(((Player) player).getEnergy() - 1);
            return;
        }

        int PlayerX = player.getPosX(), PlayerY = player.getPosY();
        int HeliX = this.getPosX(), HeliY = this.getPosY();
        int xStep = 0, yStep = 0;
        xStep = PlayerX - HeliX;
        yStep = PlayerY - HeliY;
        if(xStep != 0){
            xStep = xStep / Math.abs(xStep);
        }
        if(yStep != 0) {
            yStep = yStep / Math.abs(yStep);
        }
        this.setPosition(HeliX + xStep,HeliY + yStep);
    }

    public void searchAndDestroy(){
        new Loop<>(new Invoke<>(this::searchingAndDestroying)).scheduleFor(this);
    }
}
