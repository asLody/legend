package com.lody.legend;

import com.lody.legend.art.ArtMethod;
import com.lody.legend.dalvik.DalvikMethodStruct;
import com.lody.legend.utility.Logger;
import com.lody.legend.utility.Memory;
import com.lody.legend.utility.Runtime;
import com.lody.legend.utility.Struct;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import libcore.io.SizeOf;

/**
 * @author Lody
 * @version 1.0
 */
public class HookManager {

    /**
     * HookInfo struct size in Dalvik.
     */
    private static final int DVM_HOOK_INFO_SIZE =
              SizeOf.INT
            + SizeOf.INT
            + Struct.UINT_16
            + Struct.UINT_16
            + Struct.UINT_32
            + SizeOf.INT;

    /**
     * HookInfo struct size in Art.
     * 8 bytes * 9 * 2
     */
    private static final int ART_HOOK_INFO_SIZE = 72 * 2;

    /**
     * Singleton
     */
    private static final HookManager sDefault = new HookManager();

    /**
     * [Key] = MethodName
     * [Value] = BackupMethods
     */
    private final Map<String, Map<String,List<Method>>> classToBackupMethodsMapping = new ConcurrentHashMap<String, Map<String, List<Method>>>();

    public static HookManager getDefault() {
        return sDefault;
    }


    public void applyHooks(Class<?> holdClass) {
        for (Method hookMethod : holdClass.getDeclaredMethods()) {
            Hook hook = hookMethod.getAnnotation(Hook.class);
            if (hook != null) {
                String statement = hook.value();
                String[] splitValues = statement.split("::");
                if (splitValues.length == 2) {
                    String className = splitValues[0];
                    String[] methodNameWithSignature = splitValues[1].split("@");
                    if (methodNameWithSignature.length <= 2) {
                        String methodName = methodNameWithSignature[0];
                        String signature = methodNameWithSignature.length == 2 ? methodNameWithSignature[1] : "";
                        String[] paramList = signature.split("#");
                        if (paramList[0].equals("")) {
                            paramList = new String[0];
                        }
                        try {
                            Class<?> clazz = Class.forName(className);
                            boolean isResolve = false;
                            for (Method method : clazz.getDeclaredMethods()) {
                                if (method.getName().equals(methodName)) {
                                    Class<?>[] types = method.getParameterTypes();
                                    if (paramList.length == types.length) {
                                        boolean isMatch = true;
                                        for (int N = 0; N < types.length; N++) {
                                            if (!types[N].getName().equals(paramList[N])) {
                                                isMatch = false;
                                                break;
                                            }
                                        }
                                        if (isMatch) {
                                            hookMethod(method, hookMethod);
                                            isResolve = true;
                                            Logger.d("[+++] %s have hooked.", method.getName());
                                        }
                                    }
                                }
                                if (isResolve) {
                                    break;
                                }
                            }
                            if (!isResolve) {
                                Logger.e("[---] Cannot resolve Method : %s.", Arrays.toString(methodNameWithSignature));
                            }
                        } catch (Throwable e) {
                            Logger.e("[---] Error to Load Hook Method From : %s." , hookMethod.getName());
                            e.printStackTrace();
                        }

                    }else {
                        Logger.e("[---] Can't split method and signature : %s.", Arrays.toString(methodNameWithSignature));
                    }
                }else {
                    Logger.e("[---] Can't understand your statement : [%s].", statement);
                }
            }
        }
    }


    public void hookMethod(Method origin, Method hook) {
        if (origin == null) {
            throw new IllegalArgumentException("Origin method cannot be null");
        }
        if (hook == null) {
            throw new IllegalArgumentException("Hook method cannot be null");
        }
        if (!Modifier.isStatic(hook.getModifiers())) {
            throw new IllegalStateException("Hook method must be a static method.");
        }

        origin.setAccessible(true);
        hook.setAccessible(true);
        String methodName = Runtime.isArt() ? hook.getName() : origin.getName();
        Method backupMethod;
       if (Runtime.isArt()) {
           backupMethod = hookMethodArt(origin, hook);
       }else {
           backupMethod = hookMethodDalvik(origin, hook);
       }
        String className = hook.getDeclaringClass().getName();

        Map<String,List<Method>> methodNameToBackupMethodsMap = classToBackupMethodsMapping.get(className);
        if (methodNameToBackupMethodsMap == null) {
            methodNameToBackupMethodsMap = new ConcurrentHashMap<String, List<Method>>();
            classToBackupMethodsMapping.put(className, methodNameToBackupMethodsMap);
        }
        List<Method> backupList = methodNameToBackupMethodsMap.get(methodName);
        if (backupList == null) {
            backupList = new LinkedList<Method>();
            methodNameToBackupMethodsMap.put(methodName, backupList);
        }
        backupMethod.setAccessible(true);
        backupList.add(backupMethod);
    }



    private static Method hookMethodDalvik(Method origin, Method hook) {
        DalvikMethodStruct dvmOriginMethod = DalvikMethodStruct.of(origin);
        DalvikMethodStruct dvmHookMethod = DalvikMethodStruct.of(hook);

        byte[] originClassData = dvmOriginMethod.clazz.read();
        byte[] originInsnsData = dvmOriginMethod.insns.read();
        byte[] originInsSizeData = dvmOriginMethod.insSize.read();
        byte[] originRegisterSizeData = dvmOriginMethod.registersSize.read();
        byte[] originAccessFlags = dvmOriginMethod.accessFlags.read();
        byte[] originNativeFunc = dvmOriginMethod.nativeFunc.read();

        byte[] hookClassData = dvmHookMethod.clazz.read();
        byte[] hookInsnsData = dvmHookMethod.insns.read();
        byte[] hookInsSizeData = dvmHookMethod.insSize.read();
        byte[] hookRegisterSizeData = dvmHookMethod.registersSize.read();
        byte[] hookAccessFlags = dvmHookMethod.accessFlags.read();
        byte[] hookNativeFunc = dvmHookMethod.nativeFunc.read();

        dvmOriginMethod.clazz.write(hookClassData);
        dvmOriginMethod.insns.write(hookInsnsData);
        dvmOriginMethod.insSize.write(hookInsSizeData);
        dvmOriginMethod.registersSize.write(hookRegisterSizeData);
        dvmOriginMethod.accessFlags.write(hookAccessFlags);

        ByteBuffer byteBuffer = ByteBuffer.allocate(DVM_HOOK_INFO_SIZE);
        byteBuffer.put(originClassData);
        byteBuffer.put(originInsnsData);
        byteBuffer.put(originInsSizeData);
        byteBuffer.put(originRegisterSizeData);
        byteBuffer.put(originAccessFlags);
        byteBuffer.put(originNativeFunc);
        //May leak
        long memoryAddress = Memory.alloc(DVM_HOOK_INFO_SIZE);
        Memory.write(memoryAddress, byteBuffer.array());
        dvmOriginMethod.nativeFunc.write(memoryAddress);
        return origin;
    }

    private static Method hookMethodArt(Method origin, Method hook) {
        ArtMethod artOrigin = ArtMethod.of(origin);
        ArtMethod artHook = ArtMethod.of(hook);
        Method backup = artOrigin.backup().getMethod();
        backup.setAccessible(true);
        long originPointFromQuickCompiledCode = artOrigin.getEntryPointFromQuickCompiledCode();
        long originEntryPointFromJni = artOrigin.getEntryPointFromJni();
        long originEntryPointFromInterpreter = artOrigin.getEntryPointFromInterpreter();
        long originDeclaringClass = artOrigin.getDeclaringClass();
        long originAccessFlags = artOrigin.getAccessFlags();
        long originDexCacheResolvedMethods = artOrigin.getDexCacheResolvedMethods();
        long originDexCacheResolvedTypes = artOrigin.getDexCacheResolvedTypes();
        long originDexCodeItemOffset = artOrigin.getDexCodeItemOffset();
        long originDexMethodIndex = artOrigin.getDexMethodIndex();

        long hookPointFromQuickCompiledCode = artHook.getEntryPointFromQuickCompiledCode();
        long hookEntryPointFromJni = artHook.getEntryPointFromJni();
        long hookEntryPointFromInterpreter = artHook.getEntryPointFromInterpreter();
        long hookDeclaringClass = artHook.getDeclaringClass();
        long hookAccessFlags = artHook.getAccessFlags();
        long hookDexCacheResolvedMethods = artHook.getDexCacheResolvedMethods();
        long hookDexCacheResolvedTypes = artHook.getDexCacheResolvedTypes();
        long hookDexCodeItemOffset = artHook.getDexCodeItemOffset();
        long hookDexMethodIndex = artHook.getDexMethodIndex();

        ByteBuffer hookInfo = ByteBuffer.allocate(ART_HOOK_INFO_SIZE);
        hookInfo.putLong(originPointFromQuickCompiledCode);
        hookInfo.putLong(originEntryPointFromJni);
        hookInfo.putLong(originEntryPointFromInterpreter);
        hookInfo.putLong(originDeclaringClass);
        hookInfo.putLong(originAccessFlags);
        hookInfo.putLong(originDexCacheResolvedMethods);
        hookInfo.putLong(originDexCacheResolvedTypes);
        hookInfo.putLong(originDexCodeItemOffset);
        hookInfo.putLong(originDexMethodIndex);

        hookInfo.putLong(hookPointFromQuickCompiledCode);
        hookInfo.putLong(hookEntryPointFromJni);
        hookInfo.putLong(hookEntryPointFromInterpreter);
        hookInfo.putLong(hookDeclaringClass);
        hookInfo.putLong(hookAccessFlags);
        hookInfo.putLong(hookDexCacheResolvedMethods);
        hookInfo.putLong(hookDexCacheResolvedTypes);
        hookInfo.putLong(hookDexCodeItemOffset);
        hookInfo.putLong(hookDexMethodIndex);

        artOrigin.setEntryPointFromQuickCompiledCode(hookPointFromQuickCompiledCode);
        artOrigin.setEntryPointFromInterpreter(hookEntryPointFromInterpreter);
        artOrigin.setDeclaringClass(hookDeclaringClass);
        artOrigin.setDexCacheResolvedMethods(hookDexCacheResolvedMethods);
        artOrigin.setDexCacheResolvedTypes(hookDexCacheResolvedTypes);
        artOrigin.setDexCodeItemOffset((int) hookDexCodeItemOffset);
        artOrigin.setDexMethodIndex((int) hookDexMethodIndex);

        int accessFlags = origin.getModifiers();
        if (Modifier.isNative(accessFlags)) {
            accessFlags &= ~ Modifier.NATIVE;
            artOrigin.setAccessFlags(accessFlags);
        }
        long memoryAddress = Memory.alloc(ART_HOOK_INFO_SIZE);
        Memory.write(memoryAddress,hookInfo.array());
        artOrigin.setEntryPointFromJni(memoryAddress);

        return backup;
    }


    public <T> T callSuper(Object who, Object... args) {
        StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
        StackTraceElement currentInvoking = traceElements[3];
        String invokingClassName = currentInvoking.getClassName();
        String invokingMethodName = currentInvoking.getMethodName();
        Map<String,List<Method>> methodNameToBackupMethodsMap = classToBackupMethodsMapping.get(invokingClassName);
        if (methodNameToBackupMethodsMap != null) {
            List<Method> methodList = methodNameToBackupMethodsMap.get(invokingMethodName);
            if (methodList != null) {
                Method method = matchSimilarMethod(methodList, args);
                if (method != null) {
                    try {
                       if (Runtime.isArt()) {
                           return callSuperArt(method, who, args);
                       }else {
                           return callSuperDalvik(method, who, args);
                       }
                    } catch (Throwable e) {
                        Logger.e("[---] Call super method with error : %s, detail message please see the [Logcat :system.err].", e.getMessage());
                        e.printStackTrace();
                    }
                }else {
                    Logger.e("[---] Super method cannot found in backup map.");
                }
            }
        }
        return null;
    }

    private <T> T callSuperArt(Method method, Object who, Object... args) throws Throwable {
        //noinspection unchecked
        return (T) method.invoke(who, args);
    }


    private <T> T callSuperDalvik(Method method, Object who, Object... args) throws Throwable {
        DalvikMethodStruct dvmMethod = DalvikMethodStruct.of(method);
        long memoryAddress = dvmMethod.nativeFunc.readLong();
        byte[] hookInfo = Memory.read(memoryAddress, DVM_HOOK_INFO_SIZE);
        ByteBuffer hookBuffer = ByteBuffer.wrap(hookInfo);
        byte[] originClassData = new byte[SizeOf.INT];
        byte[] originInsnsData = new byte[SizeOf.INT];
        byte[] originInsSizeData = new byte[Struct.UINT_16];
        byte[] originRegisterSizeData = new byte[Struct.UINT_16];
        byte[] originAccessFlags = new byte[Struct.UINT_32];
        byte[] originNativeFunc = new byte[SizeOf.INT];

        hookBuffer.get(originClassData);
        hookBuffer.get(originInsnsData);
        hookBuffer.get(originInsSizeData);
        hookBuffer.get(originRegisterSizeData);
        hookBuffer.get(originAccessFlags);
        hookBuffer.get(originNativeFunc);

        byte[] hookClassData = dvmMethod.clazz.read();
        byte[] hookInsnsData = dvmMethod.insns.read();
        byte[] hookInsSizeData = dvmMethod.insSize.read();
        byte[] hookRegisterSizeData = dvmMethod.registersSize.read();
        byte[] hookAccessFlags = dvmMethod.accessFlags.read();
        byte[] hookNativeFunc = dvmMethod.nativeFunc.read();

        dvmMethod.clazz.write(originClassData);
        dvmMethod.insns.write(originInsnsData);
        dvmMethod.insSize.write(originInsSizeData);
        dvmMethod.registersSize.write(originRegisterSizeData);
        dvmMethod.accessFlags.write(originAccessFlags);
        dvmMethod.nativeFunc.write(originNativeFunc);
        //noinspection unchecked
        T result = (T) method.invoke(who,args);

        dvmMethod.clazz.write(hookClassData);
        dvmMethod.insns.write(hookInsnsData);
        dvmMethod.insSize.write(hookInsSizeData);
        dvmMethod.registersSize.write(hookRegisterSizeData);
        dvmMethod.accessFlags.write(hookAccessFlags);
        dvmMethod.nativeFunc.write(hookNativeFunc);
        return result;
    }


    private Method matchSimilarMethod(List<Method> methodList, Object... args) {
        if (methodList.size() == 1) {
            //Only hold one method
                return methodList.get(0);
        }else {
            //Hold more than one methods
            Class<?>[] types = types(args);
            for (Method method : methodList) {
                if (isSimilarSignature(method.getParameterTypes(), types)) {
                    return method;
                }
            }
            return null;
        }
    }


    private static boolean isSimilarSignature(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        if (declaredTypes.length == actualTypes.length) {
            for (int i = 0; i < actualTypes.length; i++) {
                if (actualTypes[i] == NULL.class)
                    continue;
                if (wrap(declaredTypes[i]).isAssignableFrom(wrap(actualTypes[i])))
                    continue;

                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    private static Class<?> wrap(Class<?> type) {
        if (type == null) {
            return null;
        } else if (type.isPrimitive()) {
            if (boolean.class == type) {
                return Boolean.class;
            } else if (int.class == type) {
                return Integer.class;
            } else if (long.class == type) {
                return Long.class;
            } else if (short.class == type) {
                return Short.class;
            } else if (byte.class == type) {
                return Byte.class;
            } else if (double.class == type) {
                return Double.class;
            } else if (float.class == type) {
                return Float.class;
            } else if (char.class == type) {
                return Character.class;
            } else if (void.class == type) {
                return Void.class;
            }
        }

        return type;
    }



    private static Class<?>[] types(Object... values) {
        if (values == null) {
            return new Class[0];
        }

        Class<?>[] result = new Class[values.length];

        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            result[i] = value == null ? NULL.class : value.getClass();
        }

        return result;
    }

    private static final class NULL {}

}
