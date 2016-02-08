package com.lody.legend.utility;

import java.lang.reflect.Method;

/**
 *
 * Legend native helper functions.
 *
 * NOTICE: Don't edit class name of this class.
 *
 * @author Lody
 * @version 1.0
 */
public final class LegendNative {

    static {
        System.loadLibrary("legend");
    }

    /**
     * (JJI)V
     */
    public static native void  memcpy(long dest, long src, int size);

    /**
     *
     * (J[B)V
     *
     */
    public static native void memput(long dest, byte[] data);

    /**
     *
     * (JI)[B
     *
     */
    public static native byte[] memget(long address, int size);

    /**
     *
     * (JI)V
     *
     */
    public static native void munprotect(long address, int size);

    /**
     *
     * NOTICE : this memory is execute able
     * (I)J
     *
     */
    public static native long malloc(int length);

    /**
     * (J)V
     */
    public static native void free(long pointer, int length);

    /**
     * Get the address from method
     *
     * (Ljava/lang/reflect/Method;)J
     */
    public static native long getMethodAddress(Method method);

    /**
     *
     * (Ljava/lang/Object;)J
     */
    public static native long getObjectAddress(Object object);

    /**
     *
     * BigEndian or LittleEndian
     * Most of device are little endian.
     *
     * ()Z
     */
    public static native boolean isLittleEndian();

    /**
     * (J)J
     */
    public static native long getPointer(long address);

    /**
     *
     * (JI)I
     *
     */
    public static native int getCharArrayLength(long address, int limit);


    /**
     * Length of c char
     * @param address char[] address
     * @return
     */
    public static int getCharArrayLength(long address) {
        return getCharArrayLength(address,-1);
    }

}
