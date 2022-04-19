package org.starcoin.smt;

import org.junit.jupiter.api.Test;

public class HasherTests {

    @Test
    public void testHash() {
        byte[] bs = HexUtils.hexToByteArray("0076d3bc41c9f588f7fcd0d5bf4718f8f84b1c41b20882703100b9eb9413807c012767f15c8af2f2c7225d5273fdd683edc714110a987d1054697c348aed4e6cc7");
        Hasher hasher = new Sha3Digest256Hasher();
        String h = HexUtils.byteArrayToHex(hasher.hash(new Bytes(bs)).getValue());
        System.out.println(h);//da3c17cfd8be129f09b61272f8afcf42bf5b77cf7e405f5aa20c30684a205488
    }
}
