package bgu.spl.mics.application.services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerBroadcast;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private final Attack[] attacks;



    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;

    }

    /**
     * init the microservice
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(BombDestroyerBroadcast.class, (bombDestroyerBroadcast) -> {
            terminate();
        });
        ConcurrentLinkedQueue<Future<Boolean>> futures = new ConcurrentLinkedQueue<>();
        for (Attack attack : attacks) {
            AttackEvent attackEvent = new AttackEvent(attack);
            Future<Boolean> future = sendEvent(attackEvent);
            if (future != null)
                futures.add(future);
        }
        while (!futures.isEmpty()) {
            futures.poll().get();
        }
        DeactivationEvent deactivationEvent = new DeactivationEvent();
        Future<Boolean> future =  sendEvent(deactivationEvent);
        if (future != null)
            future.get();
       BombDestroyerEvent bombDestroyerEvent = new BombDestroyerEvent();
       future = sendEvent(bombDestroyerEvent);
       if (future != null)
            future.get();
       sendBroadcast(new BombDestroyerBroadcast());
    }

    /**
     * write terminating time into diary.
     */
    @Override
    protected void close() {
        Diary.getInstance().setLeiaTerminate(System.currentTimeMillis());
    }


}
