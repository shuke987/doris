// IIA-CUS-007: parser + analyzer 互斥 → FE 拒绝
suite("repro_iia_cus_007") {
    sql "DROP TABLE IF EXISTS t_iia_cus_007"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_cus_007 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english','analyzer'='my_custom'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
        assertTrue(e.getMessage().toLowerCase().contains('analyzer') && e.getMessage().toLowerCase().contains('parser'),
                   "Error msg should mention analyzer/parser mutex; got=${e.getMessage()}")
    }
    assertTrue(threw, "FE should reject specifying both parser and analyzer")
    sql "DROP TABLE IF EXISTS t_iia_cus_007"
}
