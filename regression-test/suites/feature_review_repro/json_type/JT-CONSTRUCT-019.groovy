// JT-CONSTRUCT-019: object 奇数参
suite("repro_jt_construct_019") {
    boolean threw = false
    try { sql "SELECT json_object('a',1,'b')" } catch (Exception e) { threw = true }
    assertTrue(threw, "JT-CONSTRUCT-019: should reject; observed=no exception")
}
