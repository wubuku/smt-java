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

    /**
     * Do a basic sanity check on the proof, so that a malicious proof cannot
     * cause the verifier to fatally exit (e.g. due to an index out-of-range
     * error) or cause a CPU DoS attack.
     *
     * @param th TreeHasher
     * @return
     */
    public boolean sanityCheck(TreeHasher th) {
        // Check that the number of supplied sidenodes does not exceed the maximum possible.
        if (this.sideNodes.length > th.pathSize() * 8 ||
                // Check that leaf data for non-membership proofs is the correct size.
                (this.nonMembershipLeafData != null && this.nonMembershipLeafData.getValue().length != th.leafDataSize())) {
            return false;
        }

        // Check that all supplied side nodes are the correct size.
        for (Bytes v : this.sideNodes) {
            if (v.getValue().length != th.hasherSize()) {
                return false;
            }
        }

        // Check that the sibling data hashes to the first side node if not nil
        if (this.siblingData == null || this.sideNodes.length == 0) {
            return true;
        }
        Bytes siblingHash = th.digest(this.siblingData);
        return Bytes.equals(this.sideNodes[0], siblingHash);
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
