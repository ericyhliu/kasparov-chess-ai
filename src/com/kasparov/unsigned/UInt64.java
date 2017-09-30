package com.kasparov.unsigned;

import java.math.BigInteger;

/**
 * Unsigned 64 bit number.
 *
 * @author Eric Liu
 */
public class UInt64 extends Number implements Comparable<UInt64> {

    /**
     * Maximum possible value, as a BigInteger.
     */
    public static final BigInteger MAX_VALUE = new BigInteger("18446744073709551615");

    /**
     * Maximum possible value, as a long.
     */
    public static final long MAX_LONG_VALUE = Long.MAX_VALUE;

    /**
     * Minimum possible value is 0, since it is unsigned.
     */
    public static final long MIN_VALUE = 0;

    /**
     * Least significant 4 bytes.
     */
    private long bottom;

    /**
     * Most significant 4 bytes.
     */
    private long top;

    /**
     * Value of the UInt64, as a BigInteger.
     */
    private BigInteger value;

    /**
     * Constructor to create a UInt64 from a BigInteger.
     *
     * @param value
     */
    public UInt64(BigInteger value) {
        this.value = value;
        top = this.value.shiftRight(32).and(new BigInteger("4294967295")).longValue();
        bottom = this.value.and(new BigInteger("4294967295")).longValue();
    }

    /**
     * Constructor to create a UInt64 from a long.
     *
     * @param value
     */
    public UInt64(long value) {
        this.value = new BigInteger("" + value);
        top = this.value.shiftRight(32).and(new BigInteger("4294967295")).longValue();
        bottom = this.value.and(new BigInteger("4294967295")).longValue();
    }

    /**
     * Constructor to create a UInt64 from two longs.
     *
     * @param top
     * @param bottom
     */
    public UInt64(long top, long bottom) {
        BigInteger a = new BigInteger("" + top);
        a = a.shiftLeft(32);
        a = a.add(new BigInteger("" + bottom));
        value = a;
        this.top = top;
        this.bottom = bottom;
    }

    /**
     * Create a UInt64 from a String.
     *
     * @param value
     */
    public UInt64(String value) {
        BigInteger a = new BigInteger(value);
        this.value = a;
        top = this.value.shiftRight(32).and(new BigInteger("4294967295")).longValue();
        bottom = this.value.and(new BigInteger("4294967295")).longValue();
    }

    /**
     * Least significant 4 bytes.
     */
    public long bottom() {
        return bottom;
    }

    /**
     * Most significant 4 bytes.
     */
    public long top() {
        return top;
    }

    /**
     * Compare two UInt64's.
     */
    public int compareTo(UInt64 that) {
        return this.value.compareTo(that.value);
    }

    /**
     * test two UInt64's for equality
     */
    @Override
    public boolean equals(Object that) {
        return (that instanceof UInt64) && this.value.equals(((UInt64) that).value);
    }

    /**
     * Value as a byte.
     */
    @Override
    public byte byteValue() {
        return value.byteValue();
    }

    /**
     * Value as a short.
     */
    @Override
    public short shortValue() {
        return value.shortValue();
    }

    /**
     * Value as an int.
     */
    @Override
    public int intValue() {
        return value.intValue();
    }

    /**
     * Value as a long.
     */
    @Override
    public long longValue() {
        return value.longValue();
    }

    /**
     * Value as a double.
     */
    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    /**
     * Value as a float.
     */
    @Override
    public float floatValue() {
        return value.floatValue();
    }

    /**
     * Value as a BigInteger.
     */
    public BigInteger getValue() {
        return value;
    }

    /**
     * Hashcode of the value.
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Value as a String.
     */
    @Override
    public String toString() {
        return value.toString();
    }
}
