package com.pos.util;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

public final class UuidV7 {

    private static final SecureRandom RANDOM = new SecureRandom();

    private UuidV7() {}

    public static String generate() {

        long timestamp = Instant.now().toEpochMilli();

        // 48 bits timestamp
        long msb = timestamp << 16;

        // version 7 (0111)
        msb |= 0x7000;

        // random 12 bits
        msb |= (RANDOM.nextInt(1 << 12) & 0x0FFF);

        long lsb = RANDOM.nextLong();

        // variant 10xx
        lsb &= 0x3FFFFFFFFFFFFFFFL;
        lsb |= 0x8000000000000000L;

        return new UUID(msb, lsb).toString();
    }
}
