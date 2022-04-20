package org.starcoin.smt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SparseMerkleProofTests {

    private final byte[] polyTxExistsValue = new byte[]{1};

    @Test
    public void testSparseMerkleProof() {
        Map<Bytes, Bytes> nodeStore = new HashMap<>();
        Map<Bytes, Bytes> valueStore = new HashMap<>();
        SparseMerkleTree smt = new SparseMerkleTree(nodeStore, valueStore, new Sha3Digest256Hasher());
        Bytes value;
        boolean has;

        SparseMerkleProof proof;
        Bytes newRoot;

        proof = smt.prove("testKey".getBytes(StandardCharsets.UTF_8));
        newRoot = SparseMerkleProof.updateRoot(smt.getTreeHasher(), "testKey".getBytes(StandardCharsets.UTF_8), polyTxExistsValue, proof.getSideNodes(), proof.getNonMembershipLeafData());
        System.out.println(HexUtils.byteArrayToHex(newRoot.getValue()));
        smt.update(new Bytes("testKey".getBytes(StandardCharsets.UTF_8)), new Bytes(polyTxExistsValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Assertions.assertEquals(newRoot, smt.getRoot());
        proof = smt.prove("testKey".getBytes(StandardCharsets.UTF_8));
        Assertions.assertTrue(SparseMerkleProof.verifyProof(proof, newRoot, "testKey".getBytes(StandardCharsets.UTF_8), polyTxExistsValue, smt.getTreeHasher().getHasher()));

        proof = smt.prove(new Bytes("foo".getBytes(StandardCharsets.UTF_8)));
        newRoot = SparseMerkleProof.updateRootByPath(smt.getTreeHasher(), smt.getTreeHasher().path(new Bytes("foo".getBytes(StandardCharsets.UTF_8))), new Bytes(polyTxExistsValue), proof.getSideNodes(), proof.getNonMembershipLeafData());
        System.out.println(HexUtils.byteArrayToHex(newRoot.getValue()));
        smt.update(new Bytes("foo".getBytes(StandardCharsets.UTF_8)), new Bytes(polyTxExistsValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Assertions.assertEquals(newRoot, smt.getRoot());
        proof = smt.prove("foo".getBytes(StandardCharsets.UTF_8));
        Assertions.assertTrue(SparseMerkleProof.verifyProof(proof, newRoot, "foo".getBytes(StandardCharsets.UTF_8), polyTxExistsValue, smt.getTreeHasher().getHasher()));

        proof = smt.prove(new Bytes("testKey2".getBytes(StandardCharsets.UTF_8)));
        newRoot = SparseMerkleProof.updateRootByPath(smt.getTreeHasher(), smt.getTreeHasher().digest(new Bytes("testKey2".getBytes(StandardCharsets.UTF_8))), new Bytes(polyTxExistsValue), proof.getSideNodes(), proof.getNonMembershipLeafData());
        System.out.println(HexUtils.byteArrayToHex(newRoot.getValue()));
        smt.update(new Bytes("testKey2".getBytes(StandardCharsets.UTF_8)), new Bytes(polyTxExistsValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Assertions.assertEquals(newRoot, smt.getRoot());
        proof = smt.prove("testKey2".getBytes(StandardCharsets.UTF_8));
        Assertions.assertTrue(SparseMerkleProof.verifyProof(proof, newRoot, "testKey2".getBytes(StandardCharsets.UTF_8), polyTxExistsValue, smt.getTreeHasher().getHasher()));

        proof = smt.prove("testKey3".getBytes(StandardCharsets.UTF_8));
        newRoot = SparseMerkleProof.updateRoot(smt.getTreeHasher(), "testKey3".getBytes(StandardCharsets.UTF_8), polyTxExistsValue, proof.getSideNodes(), proof.getNonMembershipLeafData());
        System.out.println(HexUtils.byteArrayToHex(newRoot.getValue()));
        smt.update("testKey3".getBytes(StandardCharsets.UTF_8), polyTxExistsValue);
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Assertions.assertEquals(newRoot, smt.getRoot());
        proof = smt.prove("testKey3".getBytes(StandardCharsets.UTF_8));
        Assertions.assertTrue(SparseMerkleProof.verifyProof(proof, newRoot, "testKey3".getBytes(StandardCharsets.UTF_8), polyTxExistsValue, smt.getTreeHasher().getHasher()));

        proof = smt.prove("testKey4".getBytes(StandardCharsets.UTF_8));
        newRoot = SparseMerkleProof.updateRoot(smt.getTreeHasher(), "testKey4".getBytes(StandardCharsets.UTF_8), polyTxExistsValue, proof.getSideNodes(), proof.getNonMembershipLeafData());
        System.out.println(HexUtils.byteArrayToHex(newRoot.getValue()));
        smt.update("testKey4".getBytes(StandardCharsets.UTF_8), polyTxExistsValue);
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Assertions.assertEquals(newRoot, smt.getRoot());
        proof = smt.prove("testKey4".getBytes(StandardCharsets.UTF_8));
        Assertions.assertTrue(SparseMerkleProof.verifyProof(proof, newRoot, "testKey4".getBytes(StandardCharsets.UTF_8), polyTxExistsValue, smt.getTreeHasher().getHasher()));

        proof = smt.prove("testKey5".getBytes(StandardCharsets.UTF_8));
        newRoot = SparseMerkleProof.updateRoot(smt.getTreeHasher(), "testKey5".getBytes(StandardCharsets.UTF_8), polyTxExistsValue, proof.getSideNodes(), proof.getNonMembershipLeafData());
        System.out.println(HexUtils.byteArrayToHex(newRoot.getValue()));
        smt.update(new Bytes("testKey5".getBytes(StandardCharsets.UTF_8)), new Bytes(polyTxExistsValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Assertions.assertEquals(newRoot, smt.getRoot());
        proof = smt.prove("testKey5".getBytes(StandardCharsets.UTF_8));
        Assertions.assertTrue(SparseMerkleProof.verifyProof(proof, newRoot, "testKey5".getBytes(StandardCharsets.UTF_8), polyTxExistsValue, smt.getTreeHasher().getHasher()));

    }
}
