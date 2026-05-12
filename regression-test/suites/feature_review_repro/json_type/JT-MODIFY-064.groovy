// JT-MODIFY-064: `json_set()` 0 参数
suite("repro_jt_modify_064") {
    boolean threw = false
    try { sql 'SELECT json_set()' } catch (Exception e) { threw = true }
    assertTrue(threw, "JT-MODIFY-064: 0-arg json_set should reject")
}
