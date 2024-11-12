package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Reactor;
import sk.tuke.kpi.oop.game.Ventilator;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.openables.LockedDoor;

import java.util.ArrayList;
import java.util.List;

public class Solder extends AbstractActor implements Usable<Ripley> {
    private List<List<String>> dialogues;
    private boolean state; //is solder saying something now?
    public Solder(Direction direction){
        Animation animation = new Animation("sprites/solder.png",32,32,0.2f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
        animation.setRotation(direction.getAngle());
        animation.stop();
        this.dialogues = new ArrayList<>();
        this.dialogInitialization();
        this.state = true;
    }

    private void dialogInitialization(){
        List<String> list1 = new ArrayList<>();
        list1.add("-Hello. How long should I wait for you?");
        list1.add("-Almost all of us died.It's just me and Peter left.\nHe's stuck in the warehouse");
        list1.add("-The storekeeper had the access card to the warehouse,\nbut he was eaten by beetles");
        list1.add("-Perhaps the access card is still in one of the beetles");
        dialogues.add(list1);
        List<String> list2 = new ArrayList<>();
        list2.add("-You found the warehouse access card, great.");
        list2.add("-Here you can find everything you need");
        list2.add("-When the bugs attacked, most of the soldiers were \nin the main compartment (where the shuttle is located)");
        list2.add("-Contact with them was severed a long time ago. \nYou must find out what happened there");
        list2.add("-First you must power the door with electricity, \nyou must turn on the reactor");
        list2.add("-The button to turn on the reactor is in the warehouse.\nDon't forget to turn on the cooler");
        dialogues.add(list2);
        List<String> list3 = new ArrayList<>();
        list3.add("-It looks like the ventilation system is not working");
        list3.add("-Do you have a hammer? Find the access card to the room\nwhere all the ventilators are located");
        list3.add("-The last time I saw her was near the shuttle");
        list3.add("-Damn, I'm out of breath. Hurry up!");
        dialogues.add(list3);
        List<String> list4 = new ArrayList<>();
        list4.add("-Are you saying alien mather is there?");
        list4.add("-Things are bad. It is impossible to kill her \nwhile the rest of the beetles are alive");
        list4.add("-Destroy all the bugs and then kill her!");
        list4.add("-Take your new Bazooka.");
        list4.add("-You can do it! In the meantime, we are worried about you");
        dialogues.add(list4);
    }

    @Override
    public void useWith(Ripley ripley) {
        if(ripley == null || getScene() == null) return;
        if(!canTalk()) return;
        int dialog_idx = 0;
        for(Actor actor : getScene().getActors()){
            if(actor instanceof LockedDoor && actor.getName().equals("storage door") && !((LockedDoor) actor).isLocked()){
                dialog_idx++;
                break;
            }
        }

        Reactor reactor = getScene().getFirstActorByType(Reactor.class);
        if(reactor != null && reactor.isOn()){
            dialog_idx++;
        }

        boolean isBroken = false;
        for(Actor actor : getScene().getActors()){
            if(actor instanceof Ventilator && !((Ventilator) actor).isOn()){
                isBroken = true;
                break;
            }
        }
        if(!isBroken && dialog_idx == 2) dialog_idx++; //all ventilators are ok

        printDialog(dialog_idx,0);
    }

    private void printDialog(int dialog_idx,int frase_idx){
        if(getScene() == null) return;
        new ActionSequence<>(
            new Invoke<>(() -> getScene().getGame().getOverlay().drawText(dialogues.get(dialog_idx).get(frase_idx), 80, 100).showFor(4)),
            new Wait<>(5),
            new Invoke<>(() -> {
                if(frase_idx != dialogues.get(dialog_idx).size() - 1){
                    this.printDialog(dialog_idx,frase_idx + 1);
                }
            })
        ).scheduleFor(this);
    }

    @Override
    public Class<Ripley> getUsingActorClass() {
        return Ripley.class;
    }

    public boolean canTalk(){
        return state;
    }
}
