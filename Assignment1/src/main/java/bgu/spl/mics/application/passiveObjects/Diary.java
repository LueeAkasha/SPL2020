package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.services.HanSoloMicroservice;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private static volatile Diary instance = null;
    private volatile AtomicInteger totalAttacks;
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactive;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;

    private Diary(){
        totalAttacks = new AtomicInteger(0);
        HanSoloFinish = 0;
        C3POFinish = 0;
        R2D2Deactive = 0;
        LeiaTerminate = 0;
        HanSoloTerminate = 0;
        C3POTerminate = 0;
        R2D2Terminate = 0;
        LandoTerminate = 0;
    }

    public static synchronized Diary getInstance(){
        if (instance == null){
            synchronized (Diary.class){
                if (instance == null){
                    instance = new Diary();
                }
            }
        }
        return instance;
    }


    /*
    * too much getters and setters for lazy testing!
     */
    public void increaseTotalAttacks() {
        this.totalAttacks.getAndIncrement();
    }

    public void setHanSoloFinish(long hanSoloFinish) {
        HanSoloFinish = hanSoloFinish;
    }

    public void setC3POFinish(long c3POFinish) {
        C3POFinish = c3POFinish;
    }

    public void setR2D2Deactive(long r2D2Deactive) {
        R2D2Deactive = r2D2Deactive;
    }


    public void setLeiaTerminate(long leiaTerminate) {
        LeiaTerminate = leiaTerminate;
    }

    public void setHanSoloTerminate(long hanSoloTerminate) {
        HanSoloTerminate = hanSoloTerminate;
    }

    public void setC3POTerminate(long c3POTerminate) {
        C3POTerminate = c3POTerminate;
    }

    public void setR2D2Terminate(long r2D2Terminate) {
        R2D2Terminate = r2D2Terminate;
    }

    public void setLandoTerminate(long landoTerminate) {
        LandoTerminate = landoTerminate;
    }
    public int getTotalAttacks() {
        return totalAttacks.intValue();
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    public long getR2D2Deactive() {
        return R2D2Deactive;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public void resetNumberAttacks() {
        this.totalAttacks = new AtomicInteger(0);
    }
}
