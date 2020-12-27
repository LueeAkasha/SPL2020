package bgu.spl.mics.application.services;


import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    private final Ewoks ewoks = Ewoks.getInstance();

    public HanSoloMicroservice() {
        super("Han");



    }


    /**
     * init the microservice
     */
    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, attackEvent -> {
            try {
                while (true) {
                    if (ewoks.availableThenAcquire(attackEvent.getAttack().getSerials()))
                        break;
                }
                Diary.getInstance().increaseTotalAttacks();
                Diary.getInstance().setHanSoloFinish(System.currentTimeMillis());
                Thread.sleep(attackEvent.getAttack().getDuration());
                ewoks.allRelease(attackEvent.getAttack().getSerials());
                complete(attackEvent, true);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        subscribeBroadcast(BombDestroyerBroadcast.class, (bombDestroyerBroadcast) -> {
            terminate();

        });
        Main.countDownLatch.countDown();
    }


    /**
     * write terminating time into diary.
     */
    @Override
    protected void close() {
        Diary.getInstance().setHanSoloTerminate(System.currentTimeMillis());
    }
}
