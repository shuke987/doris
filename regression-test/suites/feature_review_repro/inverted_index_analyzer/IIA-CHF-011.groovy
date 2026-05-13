// IIA-CHF-011: char_filter_type='char_replace' 缺 pattern → FE 拒绝
suite("repro_iia_chf_011") {
    sql "DROP TABLE IF EXISTS t_iia_chf_011"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_chf_011 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english','char_filter_type'='char_replace'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
        assertTrue(e.getMessage().toLowerCase().contains('pattern') || e.getMessage().toLowerCase().contains('missing'),
                   "Error msg should mention missing pattern; got=${e.getMessage()}")
    }
    assertTrue(threw, "FE should reject char_filter_type without pattern")
    sql "DROP TABLE IF EXISTS t_iia_chf_011"
}
