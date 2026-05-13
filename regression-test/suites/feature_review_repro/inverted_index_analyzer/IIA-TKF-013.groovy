// IIA-TKF-013: 未知 token_filter type → FE reject
suite("repro_iia_tkf_013") {
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_tkf_013"
    boolean threw = false
    try {
        sql """CREATE INVERTED INDEX ANALYZER iia_tkf_013 PROPERTIES('tokenizer'='standard','token_filter'='foo_unknown_filter')"""
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "FE should reject unknown token_filter type")
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_tkf_013"
}
