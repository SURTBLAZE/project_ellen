package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.actions.Drop;
import sk.tuke.kpi.oop.game.actions.Shift;
import sk.tuke.kpi.oop.game.actions.Take;
import sk.tuke.kpi.oop.game.actions.Use;
import sk.tuke.kpi.oop.game.items.Collectible;
import sk.tuke.kpi.oop.game.items.Usable;

import java.util.Objects;

public class KeeperController implements KeyboardListener {
    private Keeper keeper;

    public KeeperController(Keeper keeper){
        this.keeper = keeper;
    }

    @Override
    public void keyPressed(@NotNull Input.Key key) {
        //KeyboardListener.super.keyPressed(key);
        if(key == Input.Key.ENTER){
            new Take<>().scheduleFor(keeper);
        }
        else if(key == Input.Key.BACKSPACE){
            new Drop<>().scheduleFor(keeper);
        }
        else if(key == Input.Key.S) {
            new Shift<>().scheduleFor(keeper);
        }
        else if (key == Input.Key.U){
            Usable<?> usable = Objects.requireNonNull(keeper.getScene()).getActors()
                .stream()
                .filter(Usable.class::isInstance)
                .filter(keeper::intersects)
                .map(Usable.class::cast)
                .findFirst()
                .orElse(null);
            if(usable != null){
                new Use<>(usable).scheduleForIntersectingWith(keeper);
            }
        }
        else if(key == Input.Key.B){
            if(keeper.getBackpack() == null) return;
            Collectible item = keeper.getBackpack().peek();
            if(keeper.getScene() == null || item == null) return;
            keeper.getScene().getGame().getOverlay().drawText(item.getName(), 270, 50).showFor(2);
            if(item instanceof Usable){
                new Use<>((Usable<?>)item).scheduleForIntersectingWith(keeper);
            }
        }
    }
}
