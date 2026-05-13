// IIA-FLG-002: stopwords='english' → FE 拒绝（仅接受 'none'，SEV-2 #N6）
suite("repro_iia_flg_002") {
    sql "DROP TABLE IF EXISTS t_iia_flg_002"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_flg_002 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english','stopwords'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
        assertTrue(e.getMessage().toLowerCase().contains('stopwords') || e.getMessage().toLowerCase().contains('none'),
                   "Error msg should mention stopwords/none; got=${e.getMessage()}")
    }
    assertTrue(threw, "FE should reject stopwords other than 'none' (SEV-2 #N6: customization gap)")
    sql "DROP TABLE IF EXISTS t_iia_flg_002"
}
