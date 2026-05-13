// IIA-MOD-008: parser_mode 大小写敏感性
suite("repro_iia_mod_008") {
    // 'Fine_Grained' (mixed case) 应被 FE normalize 或 reject
    sql "DROP TABLE IF EXISTS t_iia_mod_008"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_mod_008 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='chinese', 'parser_mode'='Fine_Grained'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
    }
    // 实测：FE 大小写敏感，'Fine_Grained' 拒绝（只接受小写 fine_grained）
    assertTrue(threw, "FE should reject case-mixed parser_mode 'Fine_Grained'")
    sql "DROP TABLE IF EXISTS t_iia_mod_008"
}
