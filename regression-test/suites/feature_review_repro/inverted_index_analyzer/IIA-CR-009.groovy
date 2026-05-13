// IIA-CR-009: json 列 + inverted index 行为
suite("repro_iia_cr_009") {
    sql "DROP TABLE IF EXISTS t_iia_cr_009"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_cr_009 (id INT, c JSON,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "FE should reject inverted index on JSON column (parser-based)")
    sql "DROP TABLE IF EXISTS t_iia_cr_009"
}
