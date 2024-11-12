package sk.tuke.kpi.oop.game.items;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.ActorContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class Backpack implements ActorContainer<Collectible> {
    private List<Collectible> list;
    private String name;
    private int capacity;

    public Backpack(String name,int capacity){
        this.name = name;
        this.capacity = capacity;
        list = new ArrayList<>();
    }

    @Override
    public @NotNull List<Collectible> getContent() {
        return new ArrayList<>(this.list);
    }

    @NotNull
    @Override
    public Iterator<Collectible> iterator() {
        return this.list.iterator();
    }

    @Override
    public @Nullable Collectible peek() {
        if(this.list == null || this.list.isEmpty()) return null;
        return this.list.get(list.size() - 1);
    }

    @Override
    public void shift() {
        Collections.rotate(list,1);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    public boolean isEmptyBackpack(){
        return getSize() == 0;
    }

    @Override
    public void add(@NotNull Collectible actor) {
        if(this.list.size() == capacity) throw new IllegalStateException(name + " is full");
        list.add(actor);
    }

    @Override
    public void remove(@NotNull Collectible actor) {
            this.list.remove(actor);
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

