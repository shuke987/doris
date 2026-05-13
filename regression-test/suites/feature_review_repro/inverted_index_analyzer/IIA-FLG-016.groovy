// IIA-FLG-016: lower_case='True' (首字母大写) → FE 拒绝
suite("repro_iia_flg_016") {
    sql "DROP TABLE IF EXISTS t_iia_flg_016"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_flg_016 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english','lower_case'='True'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
        assertTrue(e.getMessage().toLowerCase().contains('lower_case') || e.getMessage().toLowerCase().contains('true'),
                   "Error msg should mention lower_case must be true/false; got=${e.getMessage()}")
    }
    assertTrue(threw, "FE should reject lower_case='True' (case sensitive, only lowercase 'true'/'false')")
    sql "DROP TABLE IF EXISTS t_iia_flg_016"
}
