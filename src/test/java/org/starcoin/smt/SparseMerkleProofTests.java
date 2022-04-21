package org.starcoin.smt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SparseMerkleProofTests {

    private final byte[] testOneByteValue = new byte[]{1};

    @Test
    public void testSparseMerkleProof() {
        Map<Bytes, Bytes> nodeStore = new HashMap<>();
        Map<Bytes, Bytes> valueStore = new HashMap<>();
        SparseMerkleTree smt = new SparseMerkleTree(nodeStore, valueStore, new Sha3Digest256Hasher());
        //Bytes value;
        //boolean has;
        SparseMerkleProof proof;
        Bytes newRoot;

        proof = smt.prove("testKey".getBytes(StandardCharsets.UTF_8));
        newRoot = SparseMerkleProof.updateRoot(smt.getTreeHasher(), "testKey".getBytes(StandardCharsets.UTF_8), testOneByteValue, proof.getSideNodes(), proof.getNonMembershipLeafData());
        System.out.println(HexUtils.byteArrayToHex(newRoot.getValue()));
        smt.update(new Bytes("testKey".getBytes(StandardCharsets.UTF_8)), new Bytes(testOneByteValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Assertions.assertEquals(newRoot, smt.getRoot());
        proof = smt.prove("testKey".getBytes(StandardCharsets.UTF_8));
        Assertions.assertTrue(SparseMerkleProof.verifyProof(proof, newRoot, "testKey".getBytes(StandardCharsets.UTF_8), testOneByteValue, smt.getTreeHasher().getHasher()));


        proof = smt.prove(new Bytes("foo".getBytes(StandardCharsets.UTF_8)));
        newRoot = SparseMerkleProof.updateRootByPath(smt.getTreeHasher(), smt.getTreeHasher().path(new Bytes("foo".getBytes(StandardCharsets.UTF_8))), new Bytes(testOneByteValue), proof.getSideNodes(), proof.getNonMembershipLeafData());
        System.out.println(HexUtils.byteArrayToHex(newRoot.getValue()));
        smt.update(new Bytes("foo".getBytes(StandardCharsets.UTF_8)), new Bytes(testOneByteValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Assertions.assertEquals(newRoot, smt.getRoot());
        proof = smt.prove("foo".getBytes(StandardCharsets.UTF_8));
        Assertions.assertTrue(SparseMerkleProof.verifyProof(proof, newRoot, "foo".getBytes(StandardCharsets.UTF_8), testOneByteValue, smt.getTreeHasher().getHasher()));


        proof = smt.prove(new Bytes("testKey2".getBytes(StandardCharsets.UTF_8)));
        newRoot = SparseMerkleProof.updateRootByPath(smt.getTreeHasher(), smt.getTreeHasher().digest(new Bytes("testKey2".getBytes(StandardCharsets.UTF_8))), new Bytes(testOneByteValue), proof.getSideNodes(), proof.getNonMembershipLeafData());
        System.out.println(HexUtils.byteArrayToHex(newRoot.getValue()));
        smt.update(new Bytes("testKey2".getBytes(StandardCharsets.UTF_8)), new Bytes(testOneByteValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Assertions.assertEquals(newRoot, smt.getRoot());
        proof = smt.prove("testKey2".getBytes(StandardCharsets.UTF_8));
        Assertions.assertTrue(SparseMerkleProof.verifyProof(proof, newRoot, "testKey2".getBytes(StandardCharsets.UTF_8), testOneByteValue, smt.getTreeHasher().getHasher()));


        proof = smt.prove("testKey3".getBytes(StandardCharsets.UTF_8));
        newRoot = SparseMerkleProof.updateRoot(smt.getTreeHasher(), "testKey3".getBytes(StandardCharsets.UTF_8), testOneByteValue, proof.getSideNodes(), proof.getNonMembershipLeafData());
        System.out.println(HexUtils.byteArrayToHex(newRoot.getValue()));
        smt.update("testKey3".getBytes(StandardCharsets.UTF_8), testOneByteValue);
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Assertions.assertEquals(newRoot, smt.getRoot());
        proof = smt.prove("testKey3".getBytes(StandardCharsets.UTF_8));
        Assertions.assertTrue(SparseMerkleProof.verifyProof(proof, newRoot, "testKey3".getBytes(StandardCharsets.UTF_8), testOneByteValue, smt.getTreeHasher().getHasher()));

        proof = smt.prove("testKey4".getBytes(StandardCharsets.UTF_8));
        newRoot = SparseMerkleProof.updateRoot(smt.getTreeHasher(), "testKey4".getBytes(StandardCharsets.UTF_8), testOneByteValue, proof.getSideNodes(), proof.getNonMembershipLeafData());
        System.out.println(HexUtils.byteArrayToHex(newRoot.getValue()));
        smt.update("testKey4".getBytes(StandardCharsets.UTF_8), testOneByteValue);
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Assertions.assertEquals(newRoot, smt.getRoot());
        proof = smt.prove("testKey4".getBytes(StandardCharsets.UTF_8));
        Assertions.assertTrue(SparseMerkleProof.verifyProof(proof, newRoot, "testKey4".getBytes(StandardCharsets.UTF_8), testOneByteValue, smt.getTreeHasher().getHasher()));

        proof = smt.prove("testKey5".getBytes(StandardCharsets.UTF_8));
        newRoot = SparseMerkleProof.updateRoot(smt.getTreeHasher(), "testKey5".getBytes(StandardCharsets.UTF_8), testOneByteValue, proof.getSideNodes(), proof.getNonMembershipLeafData());
        System.out.println(HexUtils.byteArrayToHex(newRoot.getValue()));
        smt.update(new Bytes("testKey5".getBytes(StandardCharsets.UTF_8)), new Bytes(testOneByteValue));
        System.out.println(HexUtils.byteArrayToHex(smt.getRoot().getValue()));
        Assertions.assertEquals(newRoot, smt.getRoot());
        proof = smt.prove("testKey5".getBytes(StandardCharsets.UTF_8));
        Assertions.assertTrue(SparseMerkleProof.verifyProof(proof, newRoot, "testKey5".getBytes(StandardCharsets.UTF_8), testOneByteValue, smt.getTreeHasher().getHasher()));
        //        try {
        //            smt.proveNonMembershipForRoot(new Bytes("testKey5".getBytes(StandardCharsets.UTF_8)), smt.getRoot());
        //            smt.proveMembershipForRoot(new Bytes("bar".getBytes(StandardCharsets.UTF_8)), smt.getRoot());
        //        } catch (SparseMerkleTree.MembershipProofException e) {
        //            e.printStackTrace();
        //        }

        // test prove non-membership and membership
        Assertions.assertThrows(SparseMerkleTree.NonMembershipProofException.class,
                () -> smt.proveNonMembershipForRoot(new Bytes("testKey5".getBytes(StandardCharsets.UTF_8)), smt.getRoot()));
        Assertions.assertThrows(SparseMerkleTree.MembershipProofException.class,
                () -> smt.proveMembershipForRoot(new Bytes("bar".getBytes(StandardCharsets.UTF_8)), smt.getRoot()));

    }

    @Test
    public void testUpdateRoot_1() {
        byte[] path = HexUtils.hexToByteArray("8b4a296734b97f3c2028326c695f076e35de3183ada9d07cb7b9a32f1451d71f");
        byte[] value = testOneByteValue;
        Bytes[] sideNodes = Bytes.toBytesArray(new byte[][]{
                HexUtils.hexToByteArray("6f9bb267d56d0feecdd121f682df52b22d366fa7652975bec3ddabe457207eab")
        });
        byte[] oldLeafData = HexUtils.hexToByteArray("0080be6638e99f15d7942bd0130b9118125010293dcc2054fdbf26bf997d0173f42767f15c8af2f2c7225d5273fdd683edc714110a987d1054697c348aed4e6cc7");
        Bytes r = SparseMerkleProof.updateRootByPath(new TreeHasher(new Sha3Digest256Hasher()), new Bytes(path), new Bytes(value),
                sideNodes, new Bytes(oldLeafData));
        System.out.println(HexUtils.byteArrayToHex(r.getValue()));
        Assertions.assertEquals(new Bytes(HexUtils.hexToByteArray("755e48a4526b0c5b3f7e26d00da398ffec97dc784777e16132681aa208b16be3")), r);
    }

    @Test
    public void testUpdateRoot_2() {
        byte[] path = HexUtils.hexToByteArray("c6281edc54637499646ddbd7e93636f91b8d3bb6974d7191452983fa6a015278"); // hash of string "testKey3"
        byte[] value = testOneByteValue;
        Bytes[] sideNodes = Bytes.toBytesArray(new byte[][]{
                HexUtils.hexToByteArray("a18880b51b4475f45c663c66e9baff5bfdf01f9e552c9cfd84cfeb2494ea0bbd"),
                HexUtils.hexToByteArray("da3c17cfd8be129f09b61272f8afcf42bf5b77cf7e405f5aa20c30684a205488")
        });
        byte[] oldLeafData = HexUtils.hexToByteArray("00c0359bc303b37a066ce3a91aa14628accb3eb5dd6ed2c49c93f7bc60d29c797e2767f15c8af2f2c7225d5273fdd683edc714110a987d1054697c348aed4e6cc7");
        Bytes r = SparseMerkleProof.updateRootByPath(new TreeHasher(new Sha3Digest256Hasher()), path, value, sideNodes, oldLeafData);
        System.out.println(HexUtils.byteArrayToHex(r.getValue()));
        Assertions.assertEquals(new Bytes(HexUtils.hexToByteArray("7a379f33e0def9fe3555bc83b4f67f0b8ac23927352829603bff53c03fc58992")), r);
    }

}
