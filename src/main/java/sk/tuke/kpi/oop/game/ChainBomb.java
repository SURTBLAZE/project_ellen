package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class ChainBomb extends TimeBomb{
    public ChainBomb(float time){
        super(time);
    }

    public void activateAll(){
            Ellipse2D.Float ellipse = new Ellipse2D.Float(this.getPosX() - 50,this.getPosY() - 50,100f,100f);
            Scene scene = getScene();

            List<Actor> list = scene.getActors();
            if(list == null) return;
            for (int i = 0;i < list.size();i++){
                Actor bomb = list.get(i);
                if(bomb instanceof ChainBomb && !((ChainBomb) bomb).isActivated()){
                    Rectangle2D.Float rectangle = new Rectangle2D.Float(bomb.getPosX() - 8 ,bomb.getPosY() - 8,bomb.getWidth(),bomb.getHeight());
                    if(ellipse.intersects(rectangle)) {
                        ((ChainBomb) bomb).activate();
                    }
                }
            }
    }
}
