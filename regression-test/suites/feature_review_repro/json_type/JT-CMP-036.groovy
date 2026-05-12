// JT-CMP-036: nereids 触发 LiteralExpr.getLongValue on JsonLiteral
suite("repro_jt_cmp_036") {
    boolean ok = true; String msg = ''
    try {
        def r = sql """SELECT id FROM (SELECT 1 id, CAST('1' AS JSONB) j UNION ALL SELECT 2, CAST('2' AS JSONB)) x ORDER BY j"""
    } catch (Exception e) {
        ok=false; msg=e.message
    }
    // accept either ok or AnalysisException; but NOT RuntimeException
    assertTrue(ok || !msg.contains('RuntimeException'), "JT-CMP-036: should not throw RuntimeException; msg=${msg}")
}
