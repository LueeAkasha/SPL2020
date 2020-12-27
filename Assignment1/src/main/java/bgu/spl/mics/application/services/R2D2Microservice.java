package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.BombDestroyerBroadcast;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */


public class R2D2Microservice extends MicroService {

    private final long duration;

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration = duration;

    }


    /**
     *  init the microservice
     */
    @Override
    protected void initialize() {
        subscribeEvent(DeactivationEvent.class , (deactivationEvent )->{
            try{
                Thread.sleep(duration);
                Diary.getInstance().setR2D2Deactive(System.currentTimeMillis());
                complete(deactivationEvent, true);
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
        Diary.getInstance().setR2D2Terminate(System.currentTimeMillis());
    }


}
