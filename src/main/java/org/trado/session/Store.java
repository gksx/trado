package org.trado.session;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class Store {
    private static final Map<UUID, TimedSession> store = new HashMap<>();
    public static final Store INSTANCE = new Store();
    private static final long TTL_SECONDS = 5L;

    static final String SESSION_IDENTIFER = "trado-session";

    private Store() {}

    private void add(Session session) {
        store.put(session.id(), new TimedSession(session, Instant.now()));
    }

    Session validate(UUID key) {
        var timedSession = store.get(key);
        if(timedSession == null) {
            var session = new Session(key, TTL_SECONDS, true);
            add(session);
            return session;
        }

        if (timedSession.isValidFor(Instant.now())) {
            add(timedSession.session());
            return timedSession.session();
        }

        store.remove(key);
        return new Session(key, TTL_SECONDS, false);
    }

    private record TimedSession(Session session, Instant instant) {

        private boolean isValidFor(Instant instant) {
            return instant.plusSeconds(session.ttl()).isBefore(instant);
        }
    }
}
