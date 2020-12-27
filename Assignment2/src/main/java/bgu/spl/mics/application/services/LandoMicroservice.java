package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.BombDestroyerBroadcast;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    private final long duration;

    public LandoMicroservice(long duration) {

        super("Lando");
        this.duration = duration;


    }

    /**
     * init the microservice
     */
    @Override
    protected void initialize() {
        subscribeEvent(BombDestroyerEvent.class , (bombDestroyerEvent) -> {
        try {
            Thread.sleep(duration);
            complete(bombDestroyerEvent, true);
        }
        catch (Exception exception){
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
        Diary.getInstance().setLandoTerminate(System.currentTimeMillis());
    }
}
