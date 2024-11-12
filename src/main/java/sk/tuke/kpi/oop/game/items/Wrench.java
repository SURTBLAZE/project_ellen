package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.DefectiveLight;

public class Wrench extends BreakableTool<DefectiveLight> implements Collectible{

    public Wrench(){
        super(2);
        Animation animation = new Animation("sprites/wrench.png",16,16);
        setAnimation(animation);
    }

    @Override
    public void useWith(DefectiveLight defectiveLight){
        if(defectiveLight == null || !defectiveLight.repair()) return;
        this.setRemainingUses(this.getRemainingUses() - 1);
        if(this.getRemainingUses() < 1){
            Scene scene = getScene();
            if(scene != null){
                scene.removeActor(this);
            }
        }
    }

    @Override
    public Class<DefectiveLight> getUsingActorClass() {
        return DefectiveLight.class;
    }
}
