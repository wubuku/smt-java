package org.starcoin.smt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BytesTests {

    @Test
    public void testBytesEquals() {
        byte[] bs1 = new byte[]{1, 2, 3, 5, 8, 9, 10, 11};
        byte[] bs2 = new byte[]{1, 2, 3, 5, 8, 9, 10, 11};
        Assertions.assertArrayEquals(bs1, bs2);
        Assertions.assertEquals(new Bytes(bs1), new Bytes(bs2));

        System.out.println(new Bytes(bs1));
        System.out.println(new Bytes(bs2).hashCode());
    }

    @Test
    public void testBytesCompareTo() {
        byte[] bs1 = new byte[]{1, 2, 3, 5, 8, 9, 10, 11};
        byte[] bs2 = new byte[]{2, 2, 3, 5, 8, 9, 10, 11};
        Assertions.assertTrue(new Bytes(bs1).compareTo(new Bytes(bs2)) < 0);

        byte[] bs3 = new byte[]{1, 2, 3, 5, 8, 9, 9, 11};
        Assertions.assertTrue(new Bytes(bs1).compareTo(new Bytes(bs3)) > 0);

        byte[] bs4 = new byte[]{1, 2, 3, 5, 8, 9, 10, 11};
        Assertions.assertEquals(0, new Bytes(bs1).compareTo(new Bytes(bs4)));
    }

    @Test
    public void testReverseBytesArray() {
        byte[] bs_0 = new byte[]{1, 2, 3, 5, 8, 9, 10, 11};
        byte[] bs_1 = new byte[]{11, 10, 9, 8, 5, 3, 2, 1};
        byte[] bs_2 = new byte[]{1, 2, 3, 5, 8, 9, 9, 11};
        Bytes[] r = Bytes.reverseBytesArray(new Bytes[]{new Bytes(bs_0), new Bytes(bs_1), new Bytes(bs_2)});
        Assertions.assertEquals(new Bytes(bs_0), r[2]);
        Assertions.assertEquals(new Bytes(bs_2), r[0]);
    }
}
