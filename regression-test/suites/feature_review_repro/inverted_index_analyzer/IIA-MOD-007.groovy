// IIA-MOD-007: parser_mode 错值 'ultra_fine' → FE 拒绝
suite("repro_iia_mod_007") {
    sql "DROP TABLE IF EXISTS t_iia_mod_007"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_mod_007 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='chinese', 'parser_mode'='ultra_fine'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
        assertTrue(e.getMessage().contains('parser_mode') || e.getMessage().contains('fine_grained') || e.getMessage().contains('coarse_grained'),
                   "Error msg should mention valid parser_mode values; got=${e.getMessage()}")
    }
    assertTrue(threw, "FE should reject parser_mode='ultra_fine'")
    sql "DROP TABLE IF EXISTS t_iia_mod_007"
}
