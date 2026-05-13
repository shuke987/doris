// IIA-PRS-012: parser 拼错 'englsh' → FE 拒绝
suite("repro_iia_prs_012") {
    sql "DROP TABLE IF EXISTS t_iia_prs_012"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_prs_012 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='englsh'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
        assertTrue(e.getMessage().toLowerCase().contains('parser'),
                   "Error msg should mention parser; got=${e.getMessage()}")
    }
    assertTrue(threw, "FE should reject parser='englsh' (typo)")
    sql "DROP TABLE IF EXISTS t_iia_prs_012"
}
