package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;

public class DrawOrder {
    public void proccessOrder(DrawStrategy drawStrategy, Scene scene){
        drawStrategy.draw(scene);
    }
}
