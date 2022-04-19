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

//    @Test
//    public void testUpdateRoot_1() {
//        byte[] path = HexUtils.hexToByteArray("8b4a296734b97f3c2028326c695f076e35de3183ada9d07cb7b9a32f1451d71f");
//        byte[] value = polyTxExistsValue;
//        Bytes[] sideNodes = new Bytes[]{
//                new Bytes(HexUtils.hexToByteArray("6f9bb267d56d0feecdd121f682df52b22d366fa7652975bec3ddabe457207eab"))
//        };
////
////        byte[] oldLeafData = HexUtils.hexToByteArray("0080be6638e99f15d7942bd0130b9118125010293dcc2054fdbf26bf997d0173f42767f15c8af2f2c7225d5273fdd683edc714110a987d1054697c348aed4e6cc7");
////        SparseMerkleTree smt = new SparseMerkleTree(nodes, values);
////        r, err :=smt.updateForRoot(path, value, sideNodes, oldLeafData)
////        if err != nil {
////            t.FailNow()
////        }
////        fmt.Println(hex.EncodeToString(r)) //755e48a4526b0c5b3f7e26d00da398ffec97dc784777e16132681aa208b16be3
//    }
}
