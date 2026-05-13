// JT-PARSE-015 (HARD RULE): 200-level nested JSONB MUST be rejected (exceeds 100-layer writer limit)
suite("repro_jt_parse_015") {
    String s = "[]"
    (1..200).each { s = "[${s}]" }
    boolean threw = false
    def r = null
    try { r = sql "SELECT jsonb_parse('${s}')" }
    catch (Exception e) { threw = true }
    // spec: writer limits to 100 layers; 200 must be rejected or NULL
    assertTrue(threw || (r != null && r[0][0] == null),
        "200-level nested JSONB MUST be rejected or return NULL; threw=${threw} r=${r}")
}
