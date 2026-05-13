// IIA-FLG-012: ignore_above=0 → FE 拒绝（must be positive）
suite("repro_iia_flg_012") {
    sql "DROP TABLE IF EXISTS t_iia_flg_012"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_flg_012 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english','ignore_above'='0'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
        assertTrue(e.getMessage().toLowerCase().contains('ignore_above') || e.getMessage().toLowerCase().contains('positive'),
                   "Error msg should mention ignore_above must be positive; got=${e.getMessage()}")
    }
    assertTrue(threw, "FE should reject ignore_above=0")
    sql "DROP TABLE IF EXISTS t_iia_flg_012"
}
