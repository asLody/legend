//
//  main.cpp
//  
//
//  Created by lody on 15/12/16.
//
//

#include "legend_native.hpp"
#include "common.h"


jint getCharArrayLength(JNIEnv * env, jclass clazz,jlong address, jint limit) {
    char * charArray = (char *)address;
    int length = 0;
    if (limit > 0) {
        while (*charArray != '\0' && length < limit) {
            length++;
            charArray++;
        }
    }else {
        while (*charArray != '\0') {
            length++;
            charArray++;
        }
    }
    
    return length;
}



jboolean isLittleEndian(JNIEnv * env, jclass clazz) {
    unsigned int num = 0,*p = &num;
    *(unsigned char *)p = 0xff;
    if (num == 0xff) {
        return JNI_TRUE;
    }
    return JNI_FALSE;
}

jlong getPointer(JNIEnv * env, jclass clazz,jlong address) {
    void * pointer = (void *)address;
    return (jlong)&pointer;
}

jlong getMethodAddress(JNIEnv * env, jclass clazz, jobject method) {
    return (jlong) env->FromReflectedMethod(method);
}

jlong getObjectAddress(JNIEnv * env, jclass clazz, jobject object) {
    return (jlong) object;
}

void android_memcpy(JNIEnv * env, jclass clazz, jlong dest, jlong src, jint size) {
    memcmp((const void *)dest, (const void *)src, (size_t)size);
}

void android_memput(JNIEnv * env, jclass clazz, jlong dest, jbyteArray src) {
    jbyte *srcPnt = env->GetByteArrayElements(src, 0);
    jsize length = env->GetArrayLength(src);
    unsigned char * destPnt = (unsigned char *)dest;
    for(int i = 0; i < length; ++i) {
        destPnt[i] = srcPnt[i];
    }
    env->ReleaseByteArrayElements(src, srcPnt, 0);
}

jbyteArray android_memget(JNIEnv *env, jclass _cls, jlong src, jint length) {
    jbyteArray dest = env->NewByteArray(length);
    if (dest == NULL) {
        return NULL;
    }
    unsigned char *destPnt = (unsigned char*)env->GetByteArrayElements(dest,0);
    unsigned char *srcPnt = (unsigned char*)src;
    for(int i = 0; i < length; ++i) {
        destPnt[i] = srcPnt[i];
    }
    env->ReleaseByteArrayElements(dest, (jbyte *)destPnt, 0);
    return dest;
}

void android_munprotect(JNIEnv *env, jclass _cls, jlong addr, jint len) {
    long pagesize = sysconf(_SC_PAGESIZE);
    long long alignment = (addr % pagesize);
    
    mprotect((void *)(addr - alignment), (size_t)(len + alignment), PROT_READ | PROT_WRITE | PROT_EXEC);
}

jlong android_malloc(JNIEnv *env, jclass _cls, jint length) {
#ifdef MAP_ANONYMOUS
    unsigned char * space = (unsigned char *)mmap(0, length, PROT_READ|PROT_WRITE|PROT_EXEC, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0);
#else
    unsigned char * space = (unsigned char *)mmap(0, length, PROT_READ|PROT_WRITE|PROT_EXEC, MAP_PRIVATE, -1, 0);
#endif

    return (jlong) space;
}

void android_free(JNIEnv *env, jclass _cls, jlong pointer,jint length) {
    munmap((void*)pointer, length);
}

/*
 * JNI registration.
 */
static JNINativeMethod gMethods[] = {
    
    { "memcpy", "(JJI)V", (void *) android_memcpy },
    { "memput", "(J[B)V", (void *) android_memput },
    { "memget", "(JI)[B", (void *) android_memget },
    { "munprotect", "(JI)V", (void *) android_munprotect},
    { "malloc", "(I)J", (void *) android_malloc},
    { "free", "(JI)V", (void *) android_free},
    { "getMethodAddress", "(Ljava/lang/reflect/Method;)J", (void *) getMethodAddress},
    { "getObjectAddress", "(Ljava/lang/Object;)J", (void *) getObjectAddress},
    { "isLittleEndian", "()Z", (void *) isLittleEndian},
    { "getCharArrayLength", "(JI)I", (void *) getCharArrayLength},
    { "getPointer", "(J)J", (void *) getPointer},
};




/*
 * Register several native methods for one class.
 */
static int registerNativeMethods(JNIEnv* env, const char* className,
                                 JNINativeMethod* gMethods, int numMethods) {
    jclass clazz;
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }
    
    return JNI_TRUE;
}
/*
 * Register native methods for all classes we know about.
 */
static int registerNatives(JNIEnv* env) {
    if (!registerNativeMethods(env, JNI_REG_CLASS, gMethods,
                               sizeof(gMethods) / sizeof(gMethods[0])))
        return JNI_FALSE;
    
    return JNI_TRUE;
}


/*
 * Set some test stuff up.
 *
 * Returns the JNI version on success, -1 on failure.
 */
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    
    if (vm->GetEnv((void**) &gEnv, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    
    if (!registerNatives(gEnv)) {
        return -1;
    }
    return JNI_VERSION_1_4;
}



