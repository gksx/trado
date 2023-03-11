package org.trado.session;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class SessionTest {


    @Test
    public void validateNewSession(){
        var key = UUID.randomUUID();
        var session = Store.INSTANCE.validate(key);
        assertTrue(session.valid());
    }

    @Test
    public void testInvalidSession() throws InterruptedException {
        var key = UUID.randomUUID();

        var session = Store.INSTANCE.validate(key);
        assertTrue(session.valid());

        Thread.sleep(2000L);
        session = Store.INSTANCE.validate(key);
        assertFalse(session.valid());
    }
}
