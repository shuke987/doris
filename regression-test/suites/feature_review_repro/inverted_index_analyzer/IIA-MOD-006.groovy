// IIA-MOD-006: parser=standard + parser_mode → FE 拒绝
suite("repro_iia_mod_006") {
    sql "DROP TABLE IF EXISTS t_iia_mod_006"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_mod_006 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='standard', 'parser_mode'='fine_grained'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "FE should reject parser_mode on parser=standard")
    sql "DROP TABLE IF EXISTS t_iia_mod_006"
}
