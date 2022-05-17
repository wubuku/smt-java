# smt-java

# smt

A Java library that implements a Sparse Merkle tree. The tree implements the same optimisations specified in the [Libra whitepaper][libra whitepaper], to reduce the number of hash operations required per tree operation to O(k) where k is the number of non-empty elements in the tree.

This library is ported from [Go version][SMT Go library].

[libra whitepaper]: https://diem-developers-components.netlify.app/papers/the-diem-blockchain/2020-05-26.pdf
[SMT Go library]: https://github.com/celestiaorg/smt
