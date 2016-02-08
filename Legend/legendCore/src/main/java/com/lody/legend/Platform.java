package com.lody.legend;

import com.lody.legend.utility.Runtime;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * Platform specific functions
 *
 * @author Lody
 * @version 1.0
 */
public abstract class Platform {

    /*package*/ static ByteOrder PLATFORM_BYTE_ORDER;
    /*package*/ static Platform PLATFORM_INTERNAL;

    static {
        PLATFORM_BYTE_ORDER = Runtime.isLittleEndian() ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
        if (Runtime.is64Bit()) {
            PLATFORM_INTERNAL = new Platform64Bit();
        }else {
            PLATFORM_INTERNAL = new Platform32Bit();
        }
    }

    public static Platform getPlatform() {
        return PLATFORM_INTERNAL;
    }

    /**
     * Convert a byte array to int,
     * Use this function to get address from memory.
     *
     * @param data byte array
     * @return long
     */
    public abstract int orderByteToInt(byte[] data);

    /**
     * Convert a byte array to long,
     * Use this function to get address from memory.
     *
     * @param data byte array
     * @return long
     */
    public abstract long orderByteToLong(byte[] data);

    public abstract byte[] orderLongToByte(long serial, int length);

    public abstract byte[] orderIntToByte(int serial);

    public abstract int getIntSize();


    static class Platform32Bit extends Platform {

        @Override
        public int orderByteToInt(byte[] data) {
            return ByteBuffer.wrap(data).order(PLATFORM_BYTE_ORDER).getInt();
        }

        @Override
        public long orderByteToLong(byte[] data) {
            return ByteBuffer.wrap(data).order(PLATFORM_BYTE_ORDER).getInt() & 0xFFFFFFFFL;
        }

        @Override
        public byte[] orderLongToByte(long serial, int length) {
            return ByteBuffer.allocate(length).order(PLATFORM_BYTE_ORDER).putInt((int) serial).array();
        }

        @Override
        public byte[] orderIntToByte(int serial) {
            return ByteBuffer.allocate(4).order(PLATFORM_BYTE_ORDER).putInt(serial).array();
        }

        @Override
        public int getIntSize() {
            return 4;
        }


    }

    static class Platform64Bit extends Platform {

        @Override
        public int orderByteToInt(byte[] data) {
            return ByteBuffer.wrap(data).order(PLATFORM_BYTE_ORDER).getInt();
        }

        @Override
        public long orderByteToLong(byte[] data) {
            return ByteBuffer.wrap(data).order(PLATFORM_BYTE_ORDER).getLong();
        }

        @Override
        public byte[] orderLongToByte(long serial, int length) {
            return ByteBuffer.allocate(length).order(PLATFORM_BYTE_ORDER).putLong(serial).array();
        }

        @Override
        public byte[] orderIntToByte(int serial) {
            return ByteBuffer.allocate(4).order(PLATFORM_BYTE_ORDER).putInt(serial).array();
        }

        @Override
        public int getIntSize() {
            return 8;
        }
    }

}
