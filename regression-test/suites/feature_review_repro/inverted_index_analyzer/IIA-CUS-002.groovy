// IIA-CUS-002: CREATE 同名 ANALYZER 第二次拒绝
suite("repro_iia_cus_002") {
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_cus_002"
    try {
        sql """CREATE INVERTED INDEX ANALYZER iia_cus_002 PROPERTIES('tokenizer'='standard')"""
        boolean threw = false
        try {
            sql """CREATE INVERTED INDEX ANALYZER iia_cus_002 PROPERTIES('tokenizer'='keyword')"""
        } catch (Exception e) {
            threw = true
        }
        assertTrue(threw, "Second CREATE with same name should fail (no IF NOT EXISTS)")
    } finally {
        try { sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_cus_002" } catch (Exception ignore) {}
    }
}
