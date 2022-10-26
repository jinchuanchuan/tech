package com.youcan.tech.config;

/**
 * SerializationException
 */
public class SerializationException extends RuntimeException {
    private static final long serialVersionUID = 2640439192137199610L;

    public SerializationException() {
        super();
    }

    public SerializationException(final String msg) {
        super(msg);
    }

    public SerializationException(final Throwable cause) {
        super(cause);
    }

    public SerializationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
