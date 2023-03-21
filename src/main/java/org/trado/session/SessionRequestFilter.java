package org.trado.session;

import java.util.UUID;

import org.trado.TradoRequest;

public class SessionRequestFilter {
    public void apply(TradoRequest tradoRequest) {
        tradoRequest.cookie(Store.SESSION_IDENTIFIER)
            .ifPresent(cookie -> {
                var session = Store.INSTANCE.validate(UUID.fromString(cookie.value()));
                tradoRequest.session(session);
            });
    }
}
