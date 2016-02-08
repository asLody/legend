package com.lody.legend.art;

import com.lody.legend.utility.StructMapping;
import com.lody.legend.utility.StructMember;

import java.lang.reflect.Method;

/**
 * ArtMethod is a Class extend Object in C++.
 *
 * @author Lody
 * @version 1.0
 */
public class ArtMethodStructV22_64Bit extends ArtMethod {

    //#### Object Start ######
    @StructMapping(offset = 0, length = 4)
    public /*uint32_t*/ StructMember klass_;

    @StructMapping(offset = 4, length = 4)
    public /*uint32*/ StructMember monitor_;

    //##### Object End #######

    @StructMapping(offset = 8)
    public /*pointer*/ StructMember declaring_class_;

    /**
     * Short cuts to declaring_class_->dex_cache_ member for fast compiled code access.
     */
    @StructMapping(offset = 16)
    public /*pointer*/ StructMember dex_cache_resolved_methods_;

    /**
     * Short cuts to declaring_class_->dex_cache_ member for fast compiled code access.
     */
    @StructMapping(offset = 24)
    public /*pointer*/ StructMember dex_cache_resolved_types_;

    @StructMapping(offset = 32)
    public /*uint32_t*/ StructMember access_flags_;

    /**
     * Dex file fields. The defining dex file is available via declaring_class_->dex_cache_
     */
    @StructMapping(offset = 36, length = 4)
    public /*uint32_t*/ StructMember dex_code_item_offset_;

    /**
     * Index into method_ids of the dex file associated with this method.
     */
    @StructMapping(offset = 40, length = 4)
    public /*uint32_t*/ StructMember dex_method_index_;

    /**
     * Entry within a dispatch table for this method. For static/direct methods the index is into
     * the declaringClass.directMethods, for virtual methods the vtable and for interface methods the
     * ifTable.
     */
    @StructMapping(offset = 44, length = 4)
    public /*uint32_t*/ StructMember method_index_;

    /**
     * Method dispatch from the interpreter invokes this pointer which may cause a bridge into
     * compiled code.
     */
    @StructMapping(offset = 48)
    public /*pointer*/ StructMember entry_point_from_interpreter_;

    /**
     * Pointer to JNI function registered to this method, or a function to resolve the JNI function.
     */
    @StructMapping(offset = 56)
    public /*pointer*/ StructMember entry_point_from_jni_;

    /**
     * Method dispatch from quick compiled code invokes this pointer which may cause bridging into
     * portable compiled code or the interpreter.
     */
    @StructMapping(offset = 64)
    public /*pointer*/ StructMember entry_point_from_quick_compiled_code_;

    /*package*/ ArtMethodStructV22_64Bit(Method method) {
        super(method);
    }

    @Override
    public long getEntryPointFromInterpreter() {
        return entry_point_from_interpreter_.readLong();
    }

    @Override
    public void setEntryPointFromInterpreter(long pointer_entry_point_from_interpreter) {
        this.entry_point_from_interpreter_.write(pointer_entry_point_from_interpreter);
    }

    @Override
    public long getEntryPointFromJni() {
        return entry_point_from_jni_.readLong();
    }

    @Override
    public void setEntryPointFromJni(long pointer_entry_point_from_jni) {
        this.entry_point_from_jni_.write(pointer_entry_point_from_jni);
    }

    @Override
    public long getEntryPointFromQuickCompiledCode() {
        return entry_point_from_quick_compiled_code_.readLong();
    }

    @Override
    public void setEntryPointFromQuickCompiledCode(long pointer_entry_point_from_quick_compiled_code) {
        this.entry_point_from_quick_compiled_code_.write(pointer_entry_point_from_quick_compiled_code);
    }

    @Override
    public int getAccessFlags() {
        return (int) access_flags_.readLong();
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
        return (int) dex_code_item_offset_.readLong();
    }

    @Override
    public void setDexCodeItemOffset(int offset) {
        dex_code_item_offset_.write(offset);
    }

    @Override
    public int getDexMethodIndex() {
        return (int) dex_method_index_.readLong();
    }

    @Override
    public void setDexMethodIndex(int index) {
        dex_method_index_.write(index);
    }

    @Override
    public int getMethodIndex() {
        return (int) method_index_.readLong();
    }

    @Override
    public void setMethodIndex(int index) {
        method_index_.write(index);
    }
}
