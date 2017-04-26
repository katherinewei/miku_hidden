
package com.hiden.biz.wechat.common.util.crypto;

import java.util.ArrayList;

public class ByteGroup {
    ArrayList<Byte> byteContainer = new ArrayList();

    public ByteGroup() {
    }

    public byte[] toBytes() {
        byte[] bytes = new byte[this.byteContainer.size()];

        for(int i = 0; i < this.byteContainer.size(); ++i) {
            bytes[i] = ((Byte)this.byteContainer.get(i)).byteValue();
        }

        return bytes;
    }

    public ByteGroup addBytes(byte[] bytes) {
        byte[] arr$ = bytes;
        int len$ = bytes.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            byte b = arr$[i$];
            this.byteContainer.add(Byte.valueOf(b));
        }

        return this;
    }

    public int size() {
        return this.byteContainer.size();
    }
}
