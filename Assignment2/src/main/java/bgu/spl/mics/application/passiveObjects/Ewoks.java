package bgu.spl.mics.application.passiveObjects;


import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private static volatile Ewoks instance = null;
    private ConcurrentHashMap<Integer, Ewok> ewoks;


    /**
     * private constructor
     */
    private Ewoks() {
        ewoks = new ConcurrentHashMap<>();
    }

    /**
     * @return instance of Ewoks singleton
     */
    public static Ewoks getInstance() {
            if (instance == null) {
                instance = new Ewoks();
            }
        return instance;
    }


    /**
     * @param ewok an ewok to add to Ewokds singleton
     */
    public void addEwok(Ewok ewok) {
        this.ewoks.put(ewok.getSerialNumber(), ewok);
    }

    /**
     * @param serials serials of requested ewoks
     * @return if all ewoks are available and has been activated.
     */
    public synchronized boolean availableThenAcquire(List<Integer> serials) {
        for (Integer serial : serials) {
            if (!ewoks.get(serial).IsAvailable())
                return false;
        }
        allAcquire(serials);
        return true;
    }

    /**
     * @param serials serials of ewoks to acquire
     */
    private synchronized void allAcquire(List<Integer> serials) {
        for (Integer serial : serials) {
            ewoks.get(serial).acquire();
        }
    }

    /**
     * @param serials serials of ewoks to realse
     */
    public void allRelease(List<Integer> serials) {
        for (int serial : serials) {
            ewoks.get(serial).release();
        }
    }
}
