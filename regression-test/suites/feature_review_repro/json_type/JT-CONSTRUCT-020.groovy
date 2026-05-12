// JT-CONSTRUCT-020: json_object NULL key 应拒绝
suite("repro_jt_construct_020") {
    boolean threw = false
    try { sql "SELECT json_object(NULL, 1)" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-CONSTRUCT-020: NULL key should reject")
}
