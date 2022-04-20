package org.starcoin.smt;

import java.util.Arrays;

import static org.starcoin.smt.SparseMerkleTree.RIGHT;

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


    public static Bytes updateRoot(TreeHasher th, byte[] key, byte[] value, Bytes[] sideNodes, Bytes oldLeafData) {
        return updateRoot(th, new Bytes(key), new Bytes(value), sideNodes, oldLeafData);
    }

    public static Bytes updateRoot(TreeHasher th, Bytes key, Bytes value, Bytes[] sideNodes, Bytes oldLeafData) {
        return updateRootByPath(th, th.path(key), value, sideNodes, oldLeafData);
    }

    public static Bytes updateRootByPath(TreeHasher th, Bytes path, Bytes value, Bytes[] sideNodes, Bytes oldLeafData) {
        Bytes pathNode0;      //pathNodes[0]
        if (oldLeafData == null || oldLeafData.getValue().length == 0) {
            pathNode0 = th.placeholder();
        } else {
            pathNode0 = th.digest(oldLeafData);
        }
        Bytes valueHash = th.digest(value);
        Pair<Bytes, Bytes> leafPair = th.digestLeaf(path, valueHash);
        Bytes currentHash = leafPair.getItem1();
        Bytes currentData = leafPair.getItem2();
        // if err := smt.nodes.Set(currentHash, currentData); err != nil {
        // 	return nil, err
        // }
        currentData = currentHash;

        // If the leaf node that sibling nodes lead to has a different actual path
        // than the leaf node being updated, we need to create an intermediate node
        // with this leaf node and the new leaf node as children.
        //
        // First, get the number of bits that the paths of the two leaf nodes share
        // in common as a prefix.
        int depth = th.pathSize() * 8;
        int commonPrefixCount;
        Bytes oldValueHash = null;
        if (oldLeafData == null || oldLeafData.getValue().length == 0) {
            commonPrefixCount = depth;
        } else {
            Bytes actualPath;
            Pair<Bytes, Bytes> p = th.parseLeaf(oldLeafData);
            actualPath = p.getItem1();
            oldValueHash = p.getItem2();
            commonPrefixCount = ByteUtils.countCommonPrefix(path.getValue(), actualPath.getValue());
        }
        if (commonPrefixCount != depth) {
            Pair<Bytes, Bytes> p;
            if (ByteUtils.getBitAtFromMSB(path.getValue(), commonPrefixCount) == RIGHT) {
                p = th.digestNode(pathNode0, currentData);
            } else {
                p = th.digestNode(currentData, pathNode0);
            }
            currentHash = p.getItem1();
            currentData = p.getItem2();
            // err := smt.nodes.Set(currentHash, currentData)
            // if err != nil {
            // 	return nil, err
            // }
            currentData = currentHash;
        } else if (oldValueHash != null) {
            // // Short-circuit if the same value is being set
            // if bytes.Equal(oldValueHash, valueHash) {
            // 	return smt.root, nil
            // }
            // // If an old leaf exists, remove it
            // if err := smt.nodes.Delete(pathNodes[0]); err != nil {
            // 	return nil, err
            // }
            // if err := smt.values.Delete(path); err != nil {
            // 	return nil, err
            // }
        }
        // // All remaining path nodes are orphaned
        // for i := 1; i < len(pathNodes); i++ {
        // 	if err := smt.nodes.Delete(pathNodes[i]); err != nil {
        // 		return nil, err
        // 	}
        // }
        // The offset from the bottom of the tree to the start of the side nodes.
        // Note: i-offsetOfSideNodes is the index into sideNodes[]
        int offsetOfSideNodes = depth - sideNodes.length;
        for (int i = 0; i < depth; i++) {
            Bytes sideNode;
            if (i - offsetOfSideNodes < 0 || sideNodes[i - offsetOfSideNodes] == null) {
                if (commonPrefixCount != depth && commonPrefixCount > depth - 1 - i) {
                    // If there are no side nodes at this height, but the number of
                    // bits that the paths of the two leaf nodes share in common is
                    // greater than this depth, then we need to build up the tree
                    // to this depth with placeholder values at siblings.
                    sideNode = th.placeholder();
                } else {
                    continue;
                }
            } else {
                sideNode = sideNodes[i - offsetOfSideNodes];
            }
            Pair<Bytes, Bytes> p;
            if (ByteUtils.getBitAtFromMSB(path.getValue(), depth - 1 - i) == RIGHT) {
                p = th.digestNode(sideNode, currentData);
            } else {
                p = th.digestNode(currentData, sideNode);
            }
            currentHash = p.getItem1();
            currentData = p.getItem2();
            // err := smt.nodes.Set(currentHash, currentData)
            // if err != nil {
            // 	return nil, err
            // }
            currentData = currentHash;
        }
        // if err := smt.values.Set(path, value); err != nil {
        // 	return nil, err
        // }
        return currentHash;
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
