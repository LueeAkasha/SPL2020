package bgu.spl.mics;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
@SuppressWarnings("rawtypes")
public class MessageBusImpl implements MessageBus {
    private static volatile MessageBusImpl instance = null;
    private final ConcurrentHashMap<MicroService, BlockingQueue<Message>> messages = new ConcurrentHashMap<MicroService, BlockingQueue<Message>>();
    private final ConcurrentHashMap<Class<? extends Event>, LinkedBlockingQueue<MicroService>> eventsMap = new ConcurrentHashMap<Class<? extends Event>, LinkedBlockingQueue<MicroService>>(); // ok
    private final ConcurrentHashMap<Class<? extends Broadcast>, BlockingQueue<MicroService>> broadcastMap = new ConcurrentHashMap<Class<? extends Broadcast>, BlockingQueue<MicroService>>();
    private final ConcurrentHashMap<Event, Future> futures = new ConcurrentHashMap<Event, Future>();


    /**
     * private constructor
     */
    private MessageBusImpl() {
    } //ok

    /**
     * @return instance of messagebus singleton
     */
    public static synchronized MessageBusImpl getInstance() {
        if (instance == null) {
            synchronized (MessageBusImpl.class) {
                if (instance == null) {
                    instance = new MessageBusImpl();
                }
            }
        }
        return instance;
    }// ok


    /**
     * @param type The type to subscribe to,
     * @param m    The subscribing micro-service.
     * @param <T> generic type of event
     */
    @Override
    public synchronized <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        if (!eventsMap.containsKey(type))
            eventsMap.putIfAbsent(type, new LinkedBlockingQueue<>());
        eventsMap.get(type).add(m);
    } // done

    /**
     * @param type The type to subscribe to.
     * @param m    The subscribing micro-service.
     */
    @Override
    public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        broadcastMap.putIfAbsent(type, new LinkedBlockingQueue<>());
        broadcastMap.get(type).add(m);

    } // done

    /**
     * @param e      The completed event.
     * @param result The resolved result of the completed event.
     * @param <T> generic type of event
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> void complete(Event<T> e, T result) {
        synchronized (futures) {
            if (futures.containsKey(e))
                futures.get(e).resolve(result);
        } // done
    }

    /**
     * @param b The message to added to the queues.
     */
    @Override
    public synchronized void sendBroadcast(Broadcast b) {
        try {
            if (broadcastMap.containsKey(b.getClass())) {
                for (MicroService microService : broadcastMap.get(b.getClass())) {
                    if (messages.get(microService) != null)
                        messages.get(microService).put(b);
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }// done

    /**
     * @param e   The event to add to the queue.
     * @param <T> generic type of future
     * @return future of event to solve
     */
    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        synchronized (eventsMap) {
            synchronized (futures) {
                try {
                    if (eventsMap.containsKey(e.getClass())) {
                        MicroService microService = eventsMap.get(e.getClass()).poll();
                        if (microService == null)
                            return null;
                        else {
                            messages.get(microService).put(e);
                            eventsMap.get(e.getClass()).put(microService);
                            Future<T> future = new Future<>();
                            futures.put(e, future);
                            return future;
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        return null;
    } // i think it's done

    /**
     * @param m the micro-service to create a queue for.
     */
    @Override
    public void register(MicroService m) {
        messages.put(m, new LinkedBlockingQueue<>());
    } // done

    /**
     * @param m the micro-service to unregister.
     */
    @Override
    public void unregister(MicroService m) {
        synchronized (eventsMap) {
            for (Class<? extends Event> next : eventsMap.keySet()) {
                eventsMap.get(next).remove(m);
            }
            synchronized (broadcastMap) {
                for (Class<? extends Broadcast> next : broadcastMap.keySet()) {
                    broadcastMap.get(next).remove(m);
                }
            }
            synchronized (messages) {
                messages.remove(m);
            }
        }

    } // done

    /**
     * @param m The micro-service requesting to take a message from its message
     *          queue.
     * @return Message from the microservice's messages queue
     * @throws InterruptedException interrupted exception
     */
    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException { // must be implemented
        if (messages.containsKey(m))
            return messages.get(m).take();
        else
            return null;
    }


    /** testing function
     * @param microService microservice object
     * @return if the microservice was registered or not.
     */
    public boolean isRegistered(MicroService microService){
        return messages.containsKey(microService);
    }

    /**  testing function
     * @param microService  microservice object
     * @param event event object "message"
     * @return if the microservice got this message
     */
    public boolean hasMessage (MicroService microService, Event event){
        if (messages.containsKey(microService)){
            if (!messages.get(microService).isEmpty())
                return messages.get(microService).contains(event);
            else
                return false;
        }

        else
            return false;
    }


    /** testing function
     * @param microService microservice object
     * @param type type of event
     * @return if the microservice subscribed the this type of events
     */
    public boolean isSubscrerOf(MicroService microService, Class<? extends Event> type){
        if (eventsMap.containsKey(type))
            return eventsMap.get(type).contains(microService);
        else
            return false;
    }
}
