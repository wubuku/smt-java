package org.starcoin.smt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SparseMerkleTreeTests {

    private final byte[] testOneByteValue = new byte[]{1};

    @Test
    public void testSmtBasic() {
        Map<Bytes, Bytes> nodeStore = new HashMap<>();
        Map<Bytes, Bytes> valueStore = new HashMap<>();
        SparseMerkleTree smt = new SparseMerkleTree(nodeStore, valueStore, new Sha3Digest256Hasher());
        Bytes value;
        boolean has;

        smt.update(new Bytes("testKey".getBytes(StandardCharsets.UTF_8)), new Bytes(testOneByteValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        smt.update(new Bytes("foo".getBytes(StandardCharsets.UTF_8)), new Bytes(testOneByteValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        smt.update(new Bytes("testKey2".getBytes(StandardCharsets.UTF_8)), new Bytes(testOneByteValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        smt.update(new Bytes("testKey3".getBytes(StandardCharsets.UTF_8)), new Bytes(testOneByteValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        // ///////////////
        smt.delete(new Bytes("testKey4".getBytes(StandardCharsets.UTF_8)));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Bytes rootAfterTestKey4Updated = smt.update(new Bytes("testKey4".getBytes(StandardCharsets.UTF_8)), new Bytes(testOneByteValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        // ///////////////
        smt.update(new Bytes("testKey5".getBytes(StandardCharsets.UTF_8)), new Bytes(testOneByteValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Bytes rootAfterTestKey5Deleted = smt.delete(new Bytes("testKey5".getBytes(StandardCharsets.UTF_8)));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Assertions.assertEquals(rootAfterTestKey4Updated, rootAfterTestKey5Deleted);

        value = smt.get(new Bytes("testKey4".getBytes(StandardCharsets.UTF_8)));
        System.out.println(value);
        Assertions.assertEquals(value, new Bytes(testOneByteValue));
        has = smt.has(new Bytes("testKey5".getBytes(StandardCharsets.UTF_8)));
        Assertions.assertFalse(has);
    }

    @Test
    public void testCompactProof() {
        Map<Bytes, Bytes> nodeStore = new HashMap<>();
        Map<Bytes, Bytes> valueStore = new HashMap<>();
        SparseMerkleTree smt = new SparseMerkleTree(nodeStore, valueStore, new Sha3Digest256Hasher());
        for (int i = 0; i < 100; i++) {
            Bytes key = new Bytes(("testKey" + UUID.randomUUID()).getBytes(StandardCharsets.UTF_8));
            SparseMerkleProof proof = smt.prove(key);
            SparseCompactMerkleProof compactedProof = SparseCompactMerkleProof.compactProof(proof, new Sha3Digest256Hasher());
            //System.out.println(compactedProof);
            SparseMerkleProof decompactedProof = SparseCompactMerkleProof.decompactProof(compactedProof, new Sha3Digest256Hasher());
            Assertions.assertEquals(proof, decompactedProof);
            smt.update(key, new Bytes(testOneByteValue));
            //System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        }
    }

}
