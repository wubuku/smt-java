package org.starcoin.smt;

import java.util.Arrays;

public class TreeHasher {
    private static final byte[] LEAF_PREFIX = new byte[]{0};
    private static final byte[] NODE_PREFIX = new byte[]{1};

    private final Hasher hasher;
    private final Bytes zeroValue;

    public TreeHasher(Hasher hasher) {
        if (hasher == null)
            throw new IllegalArgumentException();
        this.hasher = hasher;
        byte[] z = new byte[this.hasher.size()];
        //Arrays.fill(z, (byte) 0);
        this.zeroValue = new Bytes(z);
    }

    public Bytes digest(Bytes data) {
        return this.hasher.hash(data);
    }

    public Bytes path(Bytes key) {
        return this.digest(key);
    }

    public Pair<Bytes, Bytes> digestLeaf(Bytes path, Bytes leafData) {
        Bytes value = new Bytes(ByteUtils.concat(LEAF_PREFIX, path.getValue(), leafData.getValue()));
        Bytes sum = this.hasher.hash(value);
        return new Pair<>(sum, value);
    }

    public Pair<Bytes, Bytes> parseLeaf(Bytes data) {
        byte[] p = Arrays.copyOfRange(data.getValue(), LEAF_PREFIX.length, LEAF_PREFIX.length + this.pathSize());
        byte[] v = Arrays.copyOfRange(data.getValue(), LEAF_PREFIX.length + this.pathSize(), data.getValue().length);
        return new Pair<>(new Bytes(p), new Bytes(v));
    }

    public boolean isLeaf(Bytes data) {
        return new Bytes(Arrays.copyOfRange(data.getValue(), 0, LEAF_PREFIX.length)).equals(new Bytes(LEAF_PREFIX));
    }

    public Pair<Bytes, Bytes> digestNode(Bytes leftData, Bytes rightData) {
        Bytes value = new Bytes(ByteUtils.concat(NODE_PREFIX, leftData.getValue(), rightData.getValue()));
        Bytes sum = this.hasher.hash(value);
        return new Pair<>(sum, value);
    }

    public Pair<Bytes, Bytes> parseNode(Bytes data) {
        byte[] p = Arrays.copyOfRange(data.getValue(), NODE_PREFIX.length, NODE_PREFIX.length + this.pathSize());
        byte[] v = Arrays.copyOfRange(data.getValue(), NODE_PREFIX.length + this.pathSize(), data.getValue().length);
        return new Pair<>(new Bytes(p), new Bytes(v));
    }

    public int pathSize() {
        return this.hasher.size();
    }

    public Bytes placeholder() {
        return this.zeroValue;
    }

    public int leafDataSize() {
        return LEAF_PREFIX.length + pathSize() + this.hasher.size();
    }

    public int hasherSize() {
        return this.hasher.size();
    }

    public Hasher getHasher() {
        return hasher;
    }
}
