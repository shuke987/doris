// JT-CAST-041 (HARD RULE): JSONB-string → ARRAY MUST reject (no nested parse)
suite("repro_jt_cast_041") {
    boolean threw = false
    def r = null
    try { r = sql "SELECT CAST(CAST('\"[1,2]\"' AS JSONB) AS ARRAY<INT>)" }
    catch (Exception e) { threw = true }
    // spec: reject (strict) or NULL (non-strict); MUST NOT parse string scalar as array
    assertTrue(threw || (r != null && r[0][0] == null),
        "JT-CAST-041: CAST JSONB-string scalar → ARRAY MUST reject or return NULL (no nested parse); threw=${threw} r=${r}")
}
