package sk.tuke.kpi.oop.game.actions;

import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.actions.Action;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;

public class Move<A extends Movable> implements Action<A> {
    private Direction direction;
    private A actor;
    private float duration;
    private float actionTime;
    private boolean state;

    public Move(Direction direction, float duration) {
        this.direction = direction;
        this.duration = duration;
        this.state = false;
        this.actionTime = 0;
    }


    @Override
    public @Nullable A getActor() {
        return this.actor;
    }

    @Override
    public void setActor(@Nullable A actor) {
        this.actor = actor;
    }

    @Override
    public boolean isDone() {
        return state;
    }

    public void stop(){
        this.state = true;
        if(getActor() == null) return;
        getActor().stoppedMoving();
    }
    @Override
    public void execute(float deltaTime) {
            if(isDone() || this.getActor() == null) return;
            if (this.actionTime == 0) getActor().startedMoving(direction);

            this.actionTime += deltaTime;
            if (actionTime - duration >= 0) {
                this.stop();
                return;
            }
            if(actionTime >= duration){
                this.stop();
                return;
            }

            int newX = getActor().getPosX(), newY = getActor().getPosY();
            int oldX = newX, oldY = newY;
            newX += this.direction.getDx() * getActor().getSpeed();
            newY += this.direction.getDy() * getActor().getSpeed();
            getActor().setPosition(newX, newY);
            if(getActor().getScene() == null) return;
            if(getActor().getScene().getMap().intersectsWithWall(getActor())){
                getActor().setPosition(oldX, oldY);
                getActor().collidedWithWall();
            }
    }

    @Override
    public void reset() {
        this.state = false;
        this.actionTime = 0;
    }
}
