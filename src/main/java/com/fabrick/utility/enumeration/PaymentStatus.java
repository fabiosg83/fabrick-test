package com.fabrick.utility.enumeration;

/**
 *
 * @author fabio.sgroi
 */
public enum PaymentStatus {
    TO_PROCESS(0),
    PAYMENT_OK(1),
    PAYMENT_KO(2);

    private static final PaymentStatus[] VALUES = PaymentStatus.values();
    private final Integer code;

    private PaymentStatus(Integer code) {
        this.code = code;
    }

    public static PaymentStatus getStatus(Integer code) {
        return VALUES[code];
    }

    public Short getCode() {
        return this.code.shortValue();
    }
}
