package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;

public class DefectiveLight extends Light implements Repairable{

    private Disposable akcia;
    private boolean repaired;

    public DefectiveLight(){
        setState(false);
        setElectricityFlow(false);
    }
    private void defectLight(){
        repaired = false;
        int rand = (int)(Math.random() * 20); //Random number from 0 to 20
        if(rand == 1){
            this.toggle();
        }
    }

    public boolean repair(){
        if(this.repaired) return false;
        this.akcia.dispose();
        this.repaired = true;
        this.setState(true);

        this.akcia = new ActionSequence<>(
            new Wait<>(10),
            new Loop<>(new Invoke<>(this::defectLight))
        ).scheduleFor(this);

        return true;
    }

    public boolean isRepaired() {
        return repaired;
    }

    @Override
    public void addedToScene(Scene scene) {
        if (scene == null) return;

        super.addedToScene(scene);
        this.akcia = new Loop<>(new Invoke<>(DefectiveLight::defectLight)).scheduleFor(this);

        //scene.scheduleAction(new Loop<>(new Invoke<>(this::DefectLight)),this);
    }
}
