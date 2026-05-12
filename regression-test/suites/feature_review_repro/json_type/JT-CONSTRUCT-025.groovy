// JT-CONSTRUCT-025: object key 1KB
suite("repro_jt_construct_025") {
    String k = 'a' * 1024
    boolean threw=false
    try { sql "SELECT json_object('${k}', 1)" } catch (Exception e) { threw=true }
    assertTrue(threw, "JT-CONSTRUCT-025: 1KB key should reject")
}
