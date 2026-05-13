// IIA-CUS-001: CREATE ANALYZER 基础 + SHOW + DROP
suite("repro_iia_cus_001") {
    sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_cus_001"
    try {
        sql """CREATE INVERTED INDEX ANALYZER iia_cus_001 PROPERTIES('tokenizer'='standard','token_filter'='lowercase')"""
        def r = sql "SHOW INVERTED INDEX ANALYZER"
        boolean found = false
        for (def row : r) {
            if (row[1].toString() == "iia_cus_001") found = true
        }
        assertTrue(found, "SHOW INVERTED INDEX ANALYZER should list iia_cus_001")
    } finally {
        try { sql "DROP INVERTED INDEX ANALYZER IF EXISTS iia_cus_001" } catch (Exception ignore) {}
    }
}
