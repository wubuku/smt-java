package org.starcoin.smt;

import java.util.Arrays;

/**
 * SparseMerkleProof is a Merkle proof for an element in a SparseMerkleTree.
 */
public class SparseMerkleProof {

    /**
     * SideNodes is an array of the side nodes leading up to the leaf of the proof.
     */
    private Bytes[] sideNodes;


    /**
     * NonMembershipLeafData is the data of the unrelated leaf at the position
     * of the key being proven, in the case of a non-membership proof. For
     * membership proofs, is nil.
     */
    private Bytes nonMembershipLeafData;


    /**
     * SiblingData is the data of the sibling node to the leaf being proven,
     * required for updatable proofs. For un-updatable proofs, is nil.
     */
    private Bytes siblingData;

    public SparseMerkleProof(Bytes[] sideNodes, Bytes nonMembershipLeafData, Bytes siblingData) {
        this.sideNodes = sideNodes;
        this.nonMembershipLeafData = nonMembershipLeafData;
        this.siblingData = siblingData;
    }

    public Bytes[] getSideNodes() {
        return sideNodes;
    }

    public void setSideNodes(Bytes[] sideNodes) {
        this.sideNodes = sideNodes;
    }

    public Bytes getNonMembershipLeafData() {
        return nonMembershipLeafData;
    }

    public void setNonMembershipLeafData(Bytes nonMembershipLeafData) {
        this.nonMembershipLeafData = nonMembershipLeafData;
    }

    public Bytes getSiblingData() {
        return siblingData;
    }

    public void setSiblingData(Bytes siblingData) {
        this.siblingData = siblingData;
    }

    @Override
    public String toString() {
        return "SparseMerkleProof{" +
                "sideNodes=" + Arrays.toString(sideNodes) +
                ", nonMembershipLeafData=" + nonMembershipLeafData +
                ", siblingData=" + siblingData +
                '}';
    }
}
