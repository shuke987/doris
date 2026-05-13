// IIA-CR-008: struct 列 + parser → FE 拒绝
suite("repro_iia_cr_008") {
    sql "DROP TABLE IF EXISTS t_iia_cr_008"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_cr_008 (id INT, c STRUCT<a:INT,b:STRING>,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "FE should reject inverted index on STRUCT column")
    sql "DROP TABLE IF EXISTS t_iia_cr_008"
}
