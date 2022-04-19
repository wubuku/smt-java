package org.starcoin.smt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ByteUtilsTests {

    @Test
    public void testCountCommonPrefix() {
        byte[] bs4 = new byte[]{(byte) 0b00010000};
        byte[] bs3 = new byte[]{(byte) 0b00100000};
        Assertions.assertEquals(2, ByteUtils.countCommonPrefix(bs4, bs3));
    }

    @Test
    public void TestSetBitAtFromMSB() {
        byte[] bs5 = new byte[]{(byte) 0b00000000, (byte) 0b00001000};
        ByteUtils.setBitAtFromMSB(bs5, 8 + 5);
        int b5 = ByteUtils.getBitAtFromMSB(bs5, 8 + 5);
        Assertions.assertEquals(1, b5);
    }

    @Test
    public void testGetBitAtFromMSB() {
        byte[] bs5 = new byte[]{(byte) 0b00000000, (byte) 0b00001000};
        int b5 = ByteUtils.getBitAtFromMSB(bs5, 8 + 4);
        Assertions.assertEquals(1, b5);

        byte[] bs4 = new byte[]{(byte) 0b00010000};
        int b4 = ByteUtils.getBitAtFromMSB(bs4, 3);
        Assertions.assertEquals(1, b4);

        byte[] bs3 = new byte[]{(byte) 0b00100000};
        int b3 = ByteUtils.getBitAtFromMSB(bs3, 2);
        Assertions.assertEquals(1, b3);

        byte[] bs2 = new byte[]{(byte) 0b01000000};
        int b2 = ByteUtils.getBitAtFromMSB(bs2, 1);
        Assertions.assertEquals(1, b2);

        byte[] bs1 = new byte[]{(byte) 0b10000000};
        int b1 = ByteUtils.getBitAtFromMSB(bs1, 0);
        Assertions.assertEquals(1, b1);
    }
}
