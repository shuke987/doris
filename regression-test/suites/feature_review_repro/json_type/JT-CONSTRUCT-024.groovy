// JT-CONSTRUCT-024: object key 长 256
suite("repro_jt_construct_024") {
    String k = 'a' * 256
    boolean threw=false
    try { sql "SELECT json_object('${k}', 1)" } catch (Exception e) { threw=true }
    assertTrue(threw, "JT-CONSTRUCT-024: key=256 bytes should reject")
}
