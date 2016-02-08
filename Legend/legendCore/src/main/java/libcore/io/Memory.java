package libcore.io;

import java.nio.ByteOrder;

/**
 * Unsafe access to memory.
 *
 * ###################################################
 * ##ClassLoader will not use this version of class.##
 * ###################################################
 *
 *
 */
public final class Memory {

    private Memory() { }

    /**
     * Used to optimize nio heap buffer bulk get operations. 'dst' must be a primitive array.
     * 'dstOffset' is measured in units of 'sizeofElements' bytes.
     */
    public static native void unsafeBulkGet(Object dst, int dstOffset, int byteCount,
            byte[] src, int srcOffset, int sizeofElements, boolean swap);

    /**
     * Used to optimize nio heap buffer bulk put operations. 'src' must be a primitive array.
     * 'srcOffset' is measured in units of 'sizeofElements' bytes.
     */
    public static native void unsafeBulkPut(byte[] dst, int dstOffset, int byteCount,
            Object src, int srcOffset, int sizeofElements, boolean swap);

    public static int peekInt(byte[] src, int offset, ByteOrder order) {
        throw new RuntimeException("Stub");
    }

    public static long peekLong(byte[] src, int offset, ByteOrder order) {
        throw new RuntimeException("Stub");
    }

    public static short peekShort(byte[] src, int offset, ByteOrder order) {
        throw new RuntimeException("Stub");
    }

    public static void pokeInt(byte[] dst, int offset, int value, ByteOrder order) {
        throw new RuntimeException("Stub");
    }

    public static void pokeLong(byte[] dst, int offset, long value, ByteOrder order) {
        throw new RuntimeException("Stub");
    }

    public static void pokeShort(byte[] dst, int offset, short value, ByteOrder order) {
        throw new RuntimeException("Stub");
    }

    /**
     * Copies 'byteCount' bytes from the source to the destination. The objects are either
     * instances of DirectByteBuffer or byte[]. The offsets in the byte[] case must include
     * the Buffer.arrayOffset if the array came from a Buffer.array call. We could make this
     * private and provide the four type-safe variants, but then ByteBuffer.put(ByteBuffer)
     * would need to work out which to call based on whether the source and destination buffers
     * are direct or not.
     *
     * @hide make type-safe before making public?
     */
    public static native void memmove(Object dstObject, int dstOffset, Object srcObject, int srcOffset, long byteCount);

    public static native byte peekByte(int address);
    public static native int peekInt(int address, boolean swap);
    public static native long peekLong(int address, boolean swap);
    public static native short peekShort(int address, boolean swap);

    public static native void peekByteArray(int address, byte[] dst, int dstOffset, int byteCount);
    public static native void peekCharArray(int address, char[] dst, int dstOffset, int charCount, boolean swap);
    public static native void peekDoubleArray(int address, double[] dst, int dstOffset, int doubleCount, boolean swap);
    public static native void peekFloatArray(int address, float[] dst, int dstOffset, int floatCount, boolean swap);
    public static native void peekIntArray(int address, int[] dst, int dstOffset, int intCount, boolean swap);
    public static native void peekLongArray(int address, long[] dst, int dstOffset, int longCount, boolean swap);
    public static native void peekShortArray(int address, short[] dst, int dstOffset, int shortCount, boolean swap);

    public static native void pokeByte(int address, byte value);
    public static native void pokeInt(int address, int value, boolean swap);
    public static native void pokeLong(int address, long value, boolean swap);
    public static native void pokeShort(int address, short value, boolean swap);

    public static native void pokeByteArray(int address, byte[] src, int offset, int count);
    public static native void pokeCharArray(int address, char[] src, int offset, int count, boolean swap);
    public static native void pokeDoubleArray(int address, double[] src, int offset, int count, boolean swap);
    public static native void pokeFloatArray(int address, float[] src, int offset, int count, boolean swap);
    public static native void pokeIntArray(int address, int[] src, int offset, int count, boolean swap);
    public static native void pokeLongArray(int address, long[] src, int offset, int count, boolean swap);
    public static native void pokeShortArray(int address, short[] src, int offset, int count, boolean swap);
}