// IIA-PRS-028: 未知 PROPERTIES key → FE 拒绝
suite("repro_iia_prs_028") {
    sql "DROP TABLE IF EXISTS t_iia_prs_028"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_prs_028 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english', 'foo_bar'='bar'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
        assertTrue(e.getMessage().toLowerCase().contains('foo_bar') || e.getMessage().toLowerCase().contains('invalid'),
                   "Error msg should mention foo_bar or invalid property; got=${e.getMessage()}")
    }
    assertTrue(threw, "FE should reject unknown property 'foo_bar'")
    sql "DROP TABLE IF EXISTS t_iia_prs_028"
}
