// IIA-PRS-026: DATE 列 + parser → FE reject
suite("repro_iia_prs_026") {
    sql "DROP TABLE IF EXISTS t_iia_prs_026"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_prs_026 (id INT, dt DATE,
              INDEX dt_idx (dt) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "FE should reject parser on DATE column")
    sql "DROP TABLE IF EXISTS t_iia_prs_026"
}
