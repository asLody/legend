package com.lody.legend.dalvik;

/**
 * @author Lody
 * @version 1.0
 */
public class DalvikConstants {
    public final static int
            DALVIK_JNI_RETURN_VOID = 0, /* must be zero */
            DALVIK_JNI_RETURN_FLOAT = 1,
            DALVIK_JNI_RETURN_DOUBLE = 2,
            DALVIK_JNI_RETURN_S8 = 3,
            DALVIK_JNI_RETURN_S4 = 4,
            DALVIK_JNI_RETURN_S2 = 5,
            DALVIK_JNI_RETURN_U2 = 6,
            DALVIK_JNI_RETURN_S1 = 7,


            DALVIK_JNI_RETURN_SHIFT = 28,
            DALVIK_JNI_COUNT_SHIFT = 24,
            DALVIK_JNI_RETURN_MASK = 0x70000000,
            DALVIK_JNI_NO_ARG_INFO = 0x80000000;
}
