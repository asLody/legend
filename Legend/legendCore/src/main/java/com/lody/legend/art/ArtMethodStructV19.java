package com.lody.legend.art;

import com.lody.legend.utility.StructMapping;
import com.lody.legend.utility.StructMember;

import java.lang.reflect.Method;

/**
 * @author Lody
 * @version 1.0
 */
public class ArtMethodStructV19 extends ArtMethod {



    @StructMapping(offset = 0)
    private StructMember klass_;

    @StructMapping(offset = 4)
    private StructMember monitor_;

    @StructMapping(offset = 8)
    private StructMember declaring_class_;

    @StructMapping(offset = 12)
    private StructMember dex_cache_initialized_static_storage_;

    @StructMapping(offset = 16)
    private StructMember dex_cache_resolved_methods_;

    @StructMapping(offset = 20)
    private StructMember dex_cache_resolved_types_;

    @StructMapping(offset = 24)
    private StructMember dex_cache_strings_;

    @StructMapping(offset = 28)
    private StructMember access_flags_;

    @StructMapping(offset = 32)
    private StructMember code_item_offset_;

    @StructMapping(offset = 36)
    private StructMember core_spill_mask_;

    @StructMapping(offset = 40)
    private StructMember entry_point_from_compiled_code_;

    @StructMapping(offset = 44)
    private StructMember entry_point_from_interpreter_;

    @StructMapping(offset = 48)
    private StructMember fp_spill_mask_;

    @StructMapping(offset = 52)
    private StructMember frame_size_in_bytes_;

    @StructMapping(offset = 56)
    private StructMember gc_map_;

    @StructMapping(offset = 60)
    private StructMember mapping_table_;

    @StructMapping(offset = 64)
    private StructMember method_dex_index_;

    @StructMapping(offset = 68)
    private StructMember method_index_;

    @StructMapping(offset = 72)
    private StructMember native_method_;

    @StructMapping(offset = 76)
    private StructMember vmap_table_;

    public ArtMethodStructV19(Method method) {
        super(method);
    }

    @Override
    public long getEntryPointFromInterpreter() {
        return entry_point_from_interpreter_.readLong();
    }

    @Override
    public void setEntryPointFromInterpreter(long pointer_entry_point_from_interpreter) {
        entry_point_from_interpreter_.write(pointer_entry_point_from_interpreter);
    }

    @Override
    public long getEntryPointFromJni() {
        return native_method_.readLong();
    }

    @Override
    public void setEntryPointFromJni(long pointer_entry_point_from_jni) {
        native_method_.write(pointer_entry_point_from_jni);
    }

    @Override
    public long getEntryPointFromQuickCompiledCode() {
        return entry_point_from_compiled_code_.readLong();
    }

    @Override
    public void setEntryPointFromQuickCompiledCode(long pointer_entry_point_from_quick_compiled_code) {
        entry_point_from_compiled_code_.write(pointer_entry_point_from_quick_compiled_code);
    }

    @Override
    public int getAccessFlags() {
        return access_flags_.readInt();
    }

    @Override
    public void setAccessFlags(int newFlags) {
        access_flags_.write(newFlags);
    }

    @Override
    public long getDeclaringClass() {
        return declaring_class_.readLong();
    }

    @Override
    public void setDeclaringClass(long declaringClass) {
        declaring_class_.write(declaringClass);
    }

    @Override
    public long getDexCacheResolvedMethods() {
        return dex_cache_resolved_methods_.readLong();
    }

    @Override
    public void setDexCacheResolvedMethods(long pointer_dex_cache_resolved_methods_) {
        dex_cache_resolved_methods_.write(pointer_dex_cache_resolved_methods_);
    }

    @Override
    public long getDexCacheResolvedTypes() {
        return dex_cache_resolved_types_.readLong();
    }

    @Override
    public void setDexCacheResolvedTypes(long pointer_dex_cache_resolved_types_) {
        dex_cache_resolved_types_.write(pointer_dex_cache_resolved_types_);
    }

    @Override
    public int getDexCodeItemOffset() {
        return code_item_offset_.readInt();
    }

    @Override
    public void setDexCodeItemOffset(int offset) {
        code_item_offset_.write(offset);
    }

    @Override
    public int getDexMethodIndex() {
        return method_dex_index_.readInt();
    }

    @Override
    public void setDexMethodIndex(int index) {
        method_dex_index_.write(index);
    }

    @Override
    public int getMethodIndex() {
        return method_index_.readInt();
    }

    @Override
    public void setMethodIndex(int index) {
        method_index_.write(index);
    }
}
