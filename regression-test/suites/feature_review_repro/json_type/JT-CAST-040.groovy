// JT-CAST-040: jsonb T_Object → MAP — spec §3.5 不支持
suite("repro_jt_cast_040") {
    boolean threw = false
    try { sql "SELECT CAST(CAST('{\"a\":1}' AS JSONB) AS MAP<STRING,INT>)" }
    catch (Exception e) { threw = true }
    assertTrue(threw,
        "JT-CAST-040: object → MAP should reject (spec §3.5)")
}
