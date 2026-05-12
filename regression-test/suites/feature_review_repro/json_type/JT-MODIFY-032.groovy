// JT-MODIFY-032: json_remove path 含 wildcard 应拒绝（MySQL contract）
suite("repro_jt_modify_032") {
    boolean threw = false
    try { sql "SELECT json_remove(CAST('{\"a\":1}' AS JSONB), '\$.*')" }
    catch (Exception e) { threw = true }
    assertTrue(threw,
        "JT-MODIFY-032: wildcard path not allowed for json_remove (MySQL contract)")
}
