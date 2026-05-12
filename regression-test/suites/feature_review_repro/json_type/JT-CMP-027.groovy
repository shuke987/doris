// JT-CMP-027: partition pruning 含 jsonb 字面量
suite("repro_jt_cmp_027") {
    boolean ok = true; String msg = ''
    try {
        def r = sql """SELECT id FROM (SELECT 1 id, CAST('1' AS JSONB) j UNION ALL SELECT 2, CAST('2' AS JSONB)) x ORDER BY j"""
    } catch (Exception e) {
        ok=false; msg=e.message
    }
    // accept either ok or AnalysisException; but NOT RuntimeException
    assertTrue(ok || !msg.contains('RuntimeException'), "JT-CMP-027: should not throw RuntimeException; msg=${msg}")
}
