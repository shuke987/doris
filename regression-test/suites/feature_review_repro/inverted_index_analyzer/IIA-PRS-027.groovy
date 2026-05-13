// IIA-PRS-027: DECIMAL 列 + parser → FE reject
suite("repro_iia_prs_027") {
    sql "DROP TABLE IF EXISTS t_iia_prs_027"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_prs_027 (id INT, val DECIMAL(10,2),
              INDEX val_idx (val) USING INVERTED PROPERTIES('parser'='chinese'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "FE should reject parser on DECIMAL column")
    sql "DROP TABLE IF EXISTS t_iia_prs_027"
}
