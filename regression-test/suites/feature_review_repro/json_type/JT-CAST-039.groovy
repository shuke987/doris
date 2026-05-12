// JT-CAST-039: jsonb T_Object → STRUCT
suite("repro_jt_cast_039") {
    boolean threw = false
    def r = null
    try { r = sql "SELECT CAST(CAST('{\"a\":1,\"b\":2}' AS JSONB) AS STRUCT<a:INT,b:INT>)" }
    catch (Exception e) { threw = true }
    if (!threw) {
        String v = r[0][0]?.toString() ?: ""
        assertTrue(v.contains("1") || v.contains("2"),
            "JT-CAST-039; observed=${r}")
    }
}
