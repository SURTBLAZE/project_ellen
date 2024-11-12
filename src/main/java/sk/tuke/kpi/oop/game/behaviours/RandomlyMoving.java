package sk.tuke.kpi.oop.game.behaviours;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;

public class RandomlyMoving implements Behaviour<Movable> {

    public void randomMoving(Movable movable){
        int stepX = (int)(Math.random() * 3) - 1;
        int stepY = (int)(Math.random() * 3) - 1;

        for(Direction direction : Direction.values()){
            if(stepX == direction.getDx() && stepY == direction.getDy()){
                movable.getAnimation().setRotation(direction.getAngle());
                new Move<>(direction,2).scheduleFor(movable);
                break;
            }
        }
    }
    @Override
    public void setUp(Movable movable) {
        if(movable == null) return;
        new Loop<>(new ActionSequence<>(
            new Invoke<>(this::randomMoving),
            new Wait<>(1)
        )).scheduleFor(movable);
    }
}
