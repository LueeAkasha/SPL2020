package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
	private int serialNumber;
	private boolean available;

    /** constructor
     * @param serialNumber serial number
     */
    public Ewok(int serialNumber){
        this.serialNumber = serialNumber;
        this.available = true;
    }
    /**
     * Acquires an Ewok
     */
    public synchronized void acquire() {
            if (available) {
                available = false;
        }
    }

    /**
     * release an Ewok
     */
    public void release() {
            if (!available) {
                available = true;
            }
    }

    /**
     * @return if the ewok is available or not
     */
    public boolean IsAvailable(){
            return available;
    }

    /**
     * @return serial number of the ewok
     */
    public int getSerialNumber() {
        return serialNumber;
    }
}
