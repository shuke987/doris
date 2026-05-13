// IIA-CR-006: variant 列 + parser
// 实测 + doc 分析：V3 inverted_index_storage_format (4.1 default) ACCEPT variant + parser.
// V1 / Config.enable_inverted_index_v1_for_variant=false 路径才拒绝。本 case 锁 V3 default 行为。
// Doc gap：overview.md 未文档化 variant + inverted index 行为。
suite("repro_iia_cr_006") {
    sql "DROP TABLE IF EXISTS t_iia_cr_006"
    try {
        sql """
            CREATE TABLE t_iia_cr_006 (id INT, c VARIANT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        // 在 V3 storage 默认下应建表成功
        def r = sql "SHOW CREATE TABLE t_iia_cr_006"
        String ddl = r[0][1].toString().toLowerCase()
        assertTrue(ddl.contains("inverted") && ddl.contains("variant"),
                   "V3 storage default should accept variant + parser=english; DDL=${ddl.take(200)}")
        assertTrue(ddl.contains("inverted_index_storage_format") && ddl.contains("v3"),
                   "expected V3 storage format default; DDL=${ddl.take(200)}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_cr_006" } catch (Exception ignore) {}
    }
}
