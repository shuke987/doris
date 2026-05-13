// IIA-PRS-013: parser='English' (大写) → FE 拒绝（case sensitive）
suite("repro_iia_prs_013") {
    sql "DROP TABLE IF EXISTS t_iia_prs_013"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_prs_013 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='English'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "FE should reject parser='English' (case sensitive, only lowercase allowed)")
    sql "DROP TABLE IF EXISTS t_iia_prs_013"
}
