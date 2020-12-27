package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


class MessageBusImplTest {

    private final HanSoloMicroservice hanSoloMicroservice = new HanSoloMicroservice();
    private final LeiaMicroservice leiaMicroservice = new LeiaMicroservice(new Attack[1]);
    private final MessageBusImpl messageBus = MessageBusImpl.getInstance();
    @BeforeEach
    void setUp() {
        messageBus.register(hanSoloMicroservice);
        messageBus.register(leiaMicroservice);
    }

    @AfterEach
    void tearDown() {
        messageBus.unregister(hanSoloMicroservice);
        messageBus.unregister(leiaMicroservice);
    }

    @Test
    void subscribeEvent() {
        assertFalse(messageBus.isSubscrerOf(leiaMicroservice, BombDestroyerEvent.class));
        assertFalse(messageBus.isSubscrerOf(hanSoloMicroservice, AttackEvent.class));
        messageBus.subscribeEvent(AttackEvent.class, hanSoloMicroservice);
        messageBus.subscribeEvent(BombDestroyerEvent.class, leiaMicroservice);
        assertTrue(messageBus.isSubscrerOf(hanSoloMicroservice, AttackEvent.class));
        assertTrue(messageBus.isSubscrerOf(leiaMicroservice, BombDestroyerEvent.class));

    }

    @Test
    void complete() {
        AttackEvent event = new AttackEvent(null);
        messageBus.subscribeEvent(AttackEvent.class, hanSoloMicroservice);
        Future<Boolean> future = messageBus.sendEvent(event);
        assertNotEquals(future.get(10 , TimeUnit.MILLISECONDS), false );
        messageBus.complete(event, false);
        assertEquals(future.get(), false);
    }

    @Test
    void sendEvent() {
        Event<Boolean> event = new AttackEvent(null);
        messageBus.subscribeEvent(AttackEvent.class, hanSoloMicroservice);
        assertFalse(messageBus.hasMessage(hanSoloMicroservice, event));

        messageBus.sendEvent(event);
        assertTrue(messageBus.hasMessage(hanSoloMicroservice, event));
    }

    @Test
    void register() {
        assertTrue(messageBus.isRegistered(hanSoloMicroservice));
        assertTrue(messageBus.isRegistered(leiaMicroservice));
    }

    @Test
    void unregister() {
    }

    @Test
    void awaitMessage() {
        try {
            messageBus.unregister(leiaMicroservice);

            assertNull(messageBus.awaitMessage(leiaMicroservice));
            messageBus.subscribeEvent(AttackEvent.class, hanSoloMicroservice);
            messageBus.sendEvent(new AttackEvent(null));
            assertNotNull(messageBus.awaitMessage(hanSoloMicroservice));

        }
        catch (Exception exception){

            exception.printStackTrace();
        }
    }
}