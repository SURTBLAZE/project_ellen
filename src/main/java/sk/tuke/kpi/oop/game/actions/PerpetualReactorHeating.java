package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.oop.game.Reactor;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;

public class PerpetualReactorHeating extends AbstractAction<Reactor> {
    private int increment;
    private float timestep = 0;

    public PerpetualReactorHeating(int increment){
        this.increment = increment;
    }

    @Override
    public void execute(float deltaTime){
        Reactor reactor = getActor();
        this.timestep += deltaTime;
        if(timestep < 1.0f) return;

        if(reactor != null && reactor.getDamage() != 100){
            reactor.increaseTemperature(increment);
            this.timestep = 0;
        }
    }
}
