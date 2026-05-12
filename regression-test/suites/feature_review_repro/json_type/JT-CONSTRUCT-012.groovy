// JT-CONSTRUCT-012: json_object 奇数参数 应拒绝
suite("repro_jt_construct_012") {
    boolean threw = false
    try { sql "SELECT json_object('k')" } catch (Exception e) { threw = true }
    assertTrue(threw, "JT-CONSTRUCT-012: odd args should be rejected")
}
