package sk.tuke.kpi.oop.game.behaviours;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.messages.Topic;

import java.util.function.Predicate;

public class Observing<A extends Actor, T> implements Behaviour<A> {
    private Topic<T> topic;
    private Predicate<T> predicate;
    private Behaviour<A> delegate;
    private A actor;
    public Observing(Topic<T> topic,Predicate<T> predicate,Behaviour<A> delegate){
        this.topic = topic;
        this.predicate = predicate;
        this.delegate = delegate;
        this.actor = null;
    }

    @Override
    public void setUp(A actor) {
        if(actor == null) return;
        this.actor = actor;
        actor.getScene().getMessageBus().subscribe(topic,this::isPredicateTrue);
    }

    private void isPredicateTrue(T message){
        if(predicate.test(message)){
            delegate.setUp(actor);
        }
    }
}
