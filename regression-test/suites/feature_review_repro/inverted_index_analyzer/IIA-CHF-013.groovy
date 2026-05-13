// IIA-CHF-013: char_filter_type 未知 'fake_filter' → FE 拒绝
suite("repro_iia_chf_013") {
    sql "DROP TABLE IF EXISTS t_iia_chf_013"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_chf_013 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english',
                'char_filter_type'='fake_filter','char_filter_pattern'='.','char_filter_replacement'=' '))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "FE should reject unknown char_filter_type")
    sql "DROP TABLE IF EXISTS t_iia_chf_013"
}
