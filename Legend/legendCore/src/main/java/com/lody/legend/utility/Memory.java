package com.lody.legend.utility;

import android.util.Log;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * @author Lody
 * @version 1.0
 */
public class Memory {

    private static Unsafe THE_ONE = null;

    static {
        try {
            Field f_the_one = Unsafe.class.getDeclaredField("THE_ONE");
            f_the_one.setAccessible(true);
            THE_ONE = (Unsafe) f_the_one.get(null);
        } catch (Throwable e) {
            //Ignore
        }
    }

    public static void copy(long dest, long src, int size) {
        LegendNative.memcpy(dest,src,size);
    }

    public static void write(byte[] data, long dest){
        Log.d("#######","Write Memory to 0x" + Long.toHexString(dest));
        LegendNative.memput(dest,data);
    }

    public static void write(long dest, byte[] data){
        Log.d("#######","Write Memory to 0x" + Long.toHexString(dest));
        LegendNative.memput(dest,data);
    }

    public static byte[] read(long address, int size){
        Log.d("#######","Read Memory to 0x" + Long.toHexString(address));
        return LegendNative.memget(address,size);
    }

    public static void unlock(long address, int size){
        LegendNative.munprotect(address, size);
    }

    public static long alloc(int size){
        Log.d("#######","Malloc memory, size : " + size);
        return LegendNative.malloc(size);
    }

    public static void free(long pointer, int length) {
        Log.d("######","Free memory to 0x" + Long.toHexString(pointer));
        LegendNative.free(pointer, length);
    }

    public static Unsafe getUnsafe() {
        return THE_ONE;
    }
}
