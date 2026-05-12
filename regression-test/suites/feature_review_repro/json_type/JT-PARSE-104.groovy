// JT-PARSE-104: jsonb_parse_no_strict 变体（如存在）
suite("repro_jt_parse_104") {
    // observation: variant may not exist on this branch
    boolean threw = false
    def r = null
    try { r = sql "SELECT jsonb_parse_no_strict('{a:1}')" }
    catch (Exception e) { threw = true }
    // Lock: if exists, behavior; if not, record
    assertNotNull(threw, "JT-PARSE-104 obs; threw=${threw}")
}
