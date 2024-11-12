package sk.tuke.kpi.oop.game.openables;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.items.Usable;

public class Door extends AbstractActor implements Usable<Actor>,Openable {
    private Animation animation;
    private boolean state;
    public enum Orientation {HORIZONTAL ,VERTICAL}
    private Orientation orientation;
    public static final Topic<Door> DOOR_OPENED = Topic.create("door opened", Door.class);
    public static final Topic<Door> DOOR_CLOSED = Topic.create("door closed", Door.class);
    public Door(String name,Orientation orientation){
        super(name);
        if(orientation == Orientation.VERTICAL) {
            animation = new Animation("sprites/vdoor.png", 16, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        }
        else { //orientation == Orientation.HORIZONTAL
            animation = new Animation("sprites/hdoor.png", 32, 16, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        }
        setAnimation(animation);
        getAnimation().stop();
        state = false;
        this.orientation = orientation;
    }
    @Override
    public void useWith(Actor actor) {
        if(this.isOpen()){
            this.close();
        }
        else{
            this.open();
        }
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }

    @Override
    public void open() {
        if(!isOpen()) {
            getAnimation().setPlayMode(Animation.PlayMode.ONCE);
            getAnimation().play();
            this.state = true; //is opened

            int tileX = this.getPosX() / 16; //X position of wall
            int tileY = this.getPosY() / 16; //Y position of wall

            if(getScene() == null) return;
            MapTile mapTile = getScene().getMap().getTile(tileX ,tileY); //get this wall
            if(mapTile.isWall()){
                mapTile.setType(MapTile.Type.CLEAR); //set this wall as a clear.Ripley can go
            }
            if(this.orientation == Orientation.VERTICAL) {
                mapTile = getScene().getMap().getTile(tileX, tileY + 1); //the same
            }
            else if(this.orientation == Orientation.HORIZONTAL){
                mapTile = getScene().getMap().getTile(tileX + 1,tileY);
            }
            if(mapTile.isWall()){
                mapTile.setType(MapTile.Type.CLEAR);
            }

            getScene().getMessageBus().publish(DOOR_OPENED,this);
        }
    }

    @Override
    public void close() {
        if (isOpen()) {
            getAnimation().setPlayMode(Animation.PlayMode.ONCE_REVERSED);
            getAnimation().play();
            this.state = false;
            setWalls();
            if(getScene() == null) return;
            getScene().getMessageBus().publish(DOOR_CLOSED,this);
        }
    }

    private void setWalls(){
        int tileX = this.getPosX() / 16; //X position of wall
        int tileY = this.getPosY() / 16; //Y position of wall

        if(getScene() == null) return;
        MapTile mapTile = getScene().getMap().getTile(tileX ,tileY); //get this wall
        if(!mapTile.isWall()){
            mapTile.setType(MapTile.Type.WALL); //set this wall as a clear.Ripley can go
        }
        if(this.orientation == Orientation.VERTICAL) {
            mapTile = getScene().getMap().getTile(tileX, tileY + 1); //the same
        }
        else if(this.orientation == Orientation.HORIZONTAL){
            mapTile = getScene().getMap().getTile(tileX + 1,tileY);
        }
        if(!mapTile.isWall()){
            mapTile.setType(MapTile.Type.WALL);
        }
    }

    @Override
    public boolean isOpen() {
        return state;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        setWalls();
    }

}
