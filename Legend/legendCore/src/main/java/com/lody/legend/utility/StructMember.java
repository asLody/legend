package com.lody.legend.utility;

import com.lody.legend.Platform;

/**
 *
 * Mapping a <strong>struct member</strong> in a <strong>Struct</strong>.
 *
 * @see Struct
 *
 * @author Lody
 * @version 1.0
 */
public class StructMember {

    /**
     * Address of struct
     */
    private long structAddress;
    /**
     * Offset address of this element in struct
     */
    private long varOffsetAddress;

    /**
     * Length of this element in struct
     */
    private int length;

    public StructMember(long structAddress, long varOffsetAddress, int length) {
        this.structAddress = structAddress;
        this.varOffsetAddress = varOffsetAddress;
        this.length = length;
    }

    public long getStructAddress() {
        return structAddress;
    }

    public long getVarAddress() {
        return structAddress + varOffsetAddress;
    }

    public long getVarOffsetAddress() {
        return varOffsetAddress;
    }

    public long getLength() {
        return length;
    }


    public void write(byte[] data) {
        Memory.write(structAddress + varOffsetAddress, data);
    }

    public void write(long serialData) {
        write(Platform.getPlatform().orderLongToByte(serialData,length));
    }
    public byte[] read() {
        return LegendNative.memget(structAddress + varOffsetAddress,length);
    }

    public byte[] readInsideAsPointer(int length) {
        long address = readLong();
        return Memory.read(address,length);
    }

    public String readString(int limit) {
        long pointer = readLong();
        int length = LegendNative.getCharArrayLength(pointer,limit);
        return new String(Memory.read(pointer,length));
    }

    public String readString() {
        return readString(-1);
    }

    public int readInt() {
        return Platform.getPlatform().orderByteToInt(read());
    }

    public long readLong() {
        return Platform.getPlatform().orderByteToLong(read());
    }

}
