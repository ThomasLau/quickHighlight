package xyz.thomaslau.highlight;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class ByteBufArray {
    public static void main(String[] args) {
        testCreateByteBuf();
    }
    public static void testCreateByteBuf() {  
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(16);  
        System.out.println(buf.getClass());  

        ByteBuf heapBuf = ByteBufAllocator.DEFAULT.heapBuffer(16);  
        System.out.println(heapBuf.getClass());  

        ByteBuf directBuf = ByteBufAllocator.DEFAULT.directBuffer(16);  
        System.out.println(directBuf.getClass());  
    }
}
