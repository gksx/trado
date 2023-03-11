package org.trado.session;

import java.util.UUID;

public record Session(UUID id, long ttl, boolean valid) { }