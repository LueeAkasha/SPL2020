package bgu.spl.mics.application.passiveObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {
    private Ewok ewok;
    @BeforeEach
    void setUp() {
        ewok = new Ewok(5);
    }

    @AfterEach
    void tearDown() {
        ewok = new Ewok(5);
    }

    @Test
    void acquire() {
        assertTrue(ewok.IsAvailable());
        ewok.acquire();
        assertFalse(ewok.IsAvailable());
    }

    @Test
    void release() {
        ewok.acquire();
        assertFalse(ewok.IsAvailable()); // we can assume it bruh but to be sure..
        ewok.release();
        assertTrue(ewok.IsAvailable());
    }

    @Test
    void getSerialNumber() {
        assertEquals(ewok.getSerialNumber(), 5);
    }
}