// IIA-CR-007: map 列 + parser → FE 拒绝
suite("repro_iia_cr_007") {
    sql "DROP TABLE IF EXISTS t_iia_cr_007"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_cr_007 (id INT, c MAP<STRING,STRING>,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "FE should reject inverted index on MAP column")
    sql "DROP TABLE IF EXISTS t_iia_cr_007"
}
