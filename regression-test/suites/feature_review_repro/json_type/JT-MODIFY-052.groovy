// JT-MODIFY-052: json_remove root path '$' should fail/no-op
suite("repro_jt_modify_052") {
    boolean threw = false
    def r = null
    try { r = sql "SELECT json_remove(CAST('{\"a\":1}' AS JSONB), '\$')" }
    catch (Exception e) { threw = true }
    if (!threw) {
        // observed: no-op (returns original)
        String v = r[0][0].toString()
        assertTrue(v.contains("\"a\":1"),
            "JT-MODIFY-052: root path no-op; observed=${r}")
    }
}
