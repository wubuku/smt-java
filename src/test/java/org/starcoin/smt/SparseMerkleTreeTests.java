package org.starcoin.smt;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SparseMerkleTreeTests {

    private final byte[] polyTxExistsValue = new byte[]{1};

    @Test
    public void testSmtBasic() {
        Map<Bytes, Bytes> nodeStore = new HashMap<>();
        Map<Bytes, Bytes> valueStore = new HashMap<>();
        SparseMerkleTree smt = new SparseMerkleTree(nodeStore, valueStore, new Sha3Digest256Hasher());
        Bytes value;
        boolean has;

        smt.update(new Bytes("testKey".getBytes(StandardCharsets.UTF_8)), new Bytes(polyTxExistsValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        smt.update(new Bytes("foo".getBytes(StandardCharsets.UTF_8)), new Bytes(polyTxExistsValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        smt.update(new Bytes("testKey2".getBytes(StandardCharsets.UTF_8)), new Bytes(polyTxExistsValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        smt.update(new Bytes("testKey3".getBytes(StandardCharsets.UTF_8)), new Bytes(polyTxExistsValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        smt.update(new Bytes("testKey4".getBytes(StandardCharsets.UTF_8)), new Bytes(polyTxExistsValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        smt.update(new Bytes("testKey5".getBytes(StandardCharsets.UTF_8)), new Bytes(polyTxExistsValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
    }

}
