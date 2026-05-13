// IIA-CHF-005: char_filter_pattern UTF-8 (em-dash) → FE 拒绝 ASCII-only
suite("repro_iia_chf_005") {
    sql "DROP TABLE IF EXISTS t_iia_chf_005"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_chf_005 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english',
                'char_filter_type'='char_replace','char_filter_pattern'='—','char_filter_replacement'=' '))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
        assertTrue(e.getMessage().toLowerCase().contains('ascii'),
                   "Error msg should mention ASCII; got=${e.getMessage()}")
    }
    assertTrue(threw, "FE should reject UTF-8 char_filter_pattern (ASCII-only check)")
    sql "DROP TABLE IF EXISTS t_iia_chf_005"
}
