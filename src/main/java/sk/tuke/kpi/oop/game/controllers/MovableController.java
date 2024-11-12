package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MovableController implements KeyboardListener {
    private Movable movable;
    private Move<Movable> move;
    private Set<Direction> directions;
    private Map<Input.Key, Direction> keyDirectionMap = Map.ofEntries(
        Map.entry(Input.Key.UP, Direction.NORTH),
        Map.entry(Input.Key.DOWN, Direction.SOUTH),
        Map.entry(Input.Key.LEFT, Direction.WEST),
        Map.entry(Input.Key.RIGHT, Direction.EAST)
        );

    public MovableController(Movable movable){
        this.movable = movable;
        this.directions = new HashSet<>();
    }

    private Direction getCurrentDirection(){
        Direction combinedDirection = Direction.NONE;
        for(Direction direction : directions){
            combinedDirection = combinedDirection.combine(direction);
        }
        return combinedDirection;
    }

    @Override
    public void keyPressed(@NotNull Input.Key key) {
        if(keyDirectionMap.containsKey(key)) {
            //KeyboardListener.super.keyPressed(key);
            if(move != null){
                move.stop();
            }
            directions.add(keyDirectionMap.get(key));
            Direction combinedDirection = getCurrentDirection();
            move = new Move<>(combinedDirection,1000000);
            move.scheduleFor(movable);
           // move.setActor(movable);
          //  actionOfMove = new Loop<>(new Invoke<>(() -> move.execute(0.2f))).scheduleFor(movable);
        }
    }

    @Override
    public void keyReleased(@NotNull Input.Key key) {
        if(keyDirectionMap.containsKey(key)) {
           // KeyboardListener.super.keyReleased(key);
            directions.remove(keyDirectionMap.get(key));
            move.stop();
            if(!directions.isEmpty()) {
                Direction combinedDirection = getCurrentDirection();
                move = new Move<>(combinedDirection, 1000000);
                move.scheduleFor(movable);
            }
        }
    }

    @Override
    public void keyTyped(char character) {
        KeyboardListener.super.keyTyped(character);
    }
}
