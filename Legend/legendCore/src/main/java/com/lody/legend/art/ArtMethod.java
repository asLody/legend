package com.lody.legend.art;

import android.os.Build;

import com.lody.legend.utility.LegendNative;
import com.lody.legend.utility.Memory;
import com.lody.legend.utility.Runtime;
import com.lody.legend.utility.Struct;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Lody
 * @version 1.0
 */
public abstract class ArtMethod extends Struct {

    protected Method method;

    /**
     * NOTE: This value is only use after M.
     */
    private static int M_OBJECT_SIZE = -1;

    static {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Field f_objectSize = Class.class.getDeclaredField("objectSize");
                f_objectSize.setAccessible(true);
                M_OBJECT_SIZE = f_objectSize.getInt(Method.class);
            } catch (Throwable e) {
                //Ignore
            }
        }
    }

    public ArtMethod(Method method) {
        super(LegendNative.getMethodAddress(method));
        this.method = method;
    }

    public static ArtMethod of(Method method) {

        if (Build.VERSION.SDK_INT >= 23) {
            return  Runtime.is64Bit()
                    ? new ArtMethodStructV23_64Bit(method)
                    : new ArtMethodStructV23(method);
        }
        // The artmethod struct of Android 5.0 equals to V19,so it should not use V22.
        else if (Build.VERSION.SDK_INT > 21) {
            return Runtime.is64Bit()
                    ? new ArtMethodStructV22_64Bit(method)
                    : new ArtMethodStructV22(method);
        }
        else {
            return new ArtMethodStructV19(method);
        }
    }

    /**
     * This function used to create a substitute of this method,
     * When this method be hooked, the substitute still pointer to origin method.
     *
     * @return Backup of this method
     */
    public ArtMethod backup() {
        try {
            Class<?> abstractMethodClass = Class.forName("java.lang.reflect.AbstractMethod");
            if (Build.VERSION.SDK_INT < 23) {
                Class<?> artMethodClass = Class.forName("java.lang.reflect.ArtMethod");
                //Get the original artMethod field
                Field artMethodField = abstractMethodClass.getDeclaredField("artMethod");
                if (!artMethodField.isAccessible()) {
                    artMethodField.setAccessible(true);
                }
                Object srcArtMethod = artMethodField.get(method);

                Constructor<?> constructor = artMethodClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object destArtMethod = constructor.newInstance();

                //Fill the fields to the new method we created
                for (Field field : artMethodClass.getDeclaredFields()) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.set(destArtMethod, field.get(srcArtMethod));
                }
                Method newMethod = Method.class.getConstructor(artMethodClass).newInstance(destArtMethod);
                newMethod.setAccessible(true);
                ArtMethod artMethod = ArtMethod.of(newMethod);
                artMethod.setEntryPointFromInterpreter(getEntryPointFromInterpreter());
                artMethod.setEntryPointFromJni(getEntryPointFromJni());
                artMethod.setEntryPointFromQuickCompiledCode(getEntryPointFromQuickCompiledCode());
                //NOTICE: The clone method must set the access flags to private.
                int accessFlags = getAccessFlags();
                accessFlags &= ~Modifier.PUBLIC;
                accessFlags |= Modifier.PRIVATE;
                artMethod.setAccessFlags(accessFlags);
                return artMethod;
            }else {
                Constructor<Method> constructor = Method.class.getDeclaredConstructor();
                // we can't use constructor.setAccessible(true); because Google does not like it
                AccessibleObject.setAccessible(new AccessibleObject[]{constructor}, true);
                Method m = constructor.newInstance();
                m.setAccessible(true);
                for (Field field : abstractMethodClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(m, field.get(method));
                }
                Field artMethodField = abstractMethodClass.getDeclaredField("artMethod");
                artMethodField.setAccessible(true);
                long originArtMethod = artMethodField.getLong(method);
                long memoryAddress = Memory.alloc(M_OBJECT_SIZE);
                byte[] data = Memory.read(originArtMethod, M_OBJECT_SIZE);
                Memory.write(memoryAddress, data);
                artMethodField.set(m, memoryAddress);
                ArtMethod artMethod = ArtMethod.of(m);
                //NOTICE: The clone method must set the access flags to private.
                int accessFlags = getAccessFlags();
                accessFlags &= ~Modifier.PUBLIC;
                accessFlags |= Modifier.PRIVATE;
                artMethod.setAccessFlags(accessFlags);

                return artMethod;
            }


        } catch (Throwable e) {
            throw new IllegalStateException("Cannot create backup method from :: " + method);
        }
    }


    public Method getMethod() {
        return method;
    }


    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    ///////////////////////   Common Api   /////////////////////////////
    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////

    public abstract long getEntryPointFromInterpreter();

    public abstract void setEntryPointFromInterpreter(long pointer_entry_point_from_interpreter);

    public abstract long getEntryPointFromJni();

    public abstract void setEntryPointFromJni(long pointer_entry_point_from_jni);

    public abstract long getEntryPointFromQuickCompiledCode();

    public abstract void setEntryPointFromQuickCompiledCode(long pointer_entry_point_from_quick_compiled_code);

    public abstract int getAccessFlags();

    public abstract void setAccessFlags(int newFlags);

    public abstract long getDeclaringClass();

    public abstract void setDeclaringClass(long declaringClass);

    public abstract long getDexCacheResolvedMethods();

    public abstract void setDexCacheResolvedMethods(long pointer_dex_cache_resolved_methods_);

    public abstract long getDexCacheResolvedTypes();

    public abstract void setDexCacheResolvedTypes(long pointer_dex_cache_resolved_types_);

    public abstract int getDexCodeItemOffset();

    public abstract void setDexCodeItemOffset(int offset);

    public abstract int getDexMethodIndex();

    public abstract void setDexMethodIndex(int index);

    public abstract int getMethodIndex();

    public abstract void setMethodIndex(int index);


}
