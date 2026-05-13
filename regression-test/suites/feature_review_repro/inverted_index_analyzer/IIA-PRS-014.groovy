// IIA-PRS-014: int 列 + parser → FE 拒绝
suite("repro_iia_prs_014") {
    sql "DROP TABLE IF EXISTS t_iia_prs_014"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_prs_014 (id INT, c INT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "FE should reject parser on non-string column")
    sql "DROP TABLE IF EXISTS t_iia_prs_014"
}
