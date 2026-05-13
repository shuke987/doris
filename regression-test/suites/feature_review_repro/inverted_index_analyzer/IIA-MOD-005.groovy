// IIA-MOD-005: parser=english + parser_mode=fine_grained → FE 拒绝（regression for SEV-1 #N2 假说被 FE 拦截的事实）
suite("repro_iia_mod_005") {
    sql "DROP TABLE IF EXISTS t_iia_mod_005"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_mod_005 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english', 'parser_mode'='fine_grained'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
        assertTrue(e.getMessage().contains('parser_mode'),
                   "Error msg should mention parser_mode; got=${e.getMessage()}")
        assertTrue(e.getMessage().contains('chinese') || e.getMessage().contains('ik'),
                   "Error msg should mention chinese/ik; got=${e.getMessage()}")
    }
    assertTrue(threw, "FE should reject parser_mode on non-chinese/ik parser")
    sql "DROP TABLE IF EXISTS t_iia_mod_005"
}
