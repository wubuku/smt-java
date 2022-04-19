package org.starcoin.smt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class SmtValueStoreTests {

    @Test
    public void testCreateSmtValueStore() {
        Map<Bytes, Bytes> map = new HashMap<>();
        SmtValueStore smtValueStore = SmtValueStore.asSmtValueStore(map);
        //System.out.println(smtValueStore.isImmutable());
        smtValueStore.put(new Bytes(new byte[]{1}), new Bytes(new byte[]{1}));
        Bytes v = smtValueStore.get(new Bytes(new byte[]{1}));
        Assertions.assertEquals(v, new Bytes(new byte[]{1}));
        Assertions.assertThrows(RuntimeException.class, () -> smtValueStore.getForValueHash(v, v));
    }
}
