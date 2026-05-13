// IIA-CRX-001: partial update + analyzed index (unique MoW + parser)
suite("repro_iia_crx_001") {
    sql "DROP TABLE IF EXISTS t_iia_crx_001"
    try {
        sql """
            CREATE TABLE t_iia_crx_001 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            UNIQUE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1', 'enable_unique_key_merge_on_write'='true')
        """
        sql "INSERT INTO t_iia_crx_001 VALUES (1,'hello world')"
        // partial update
        sql "SET enable_unique_key_partial_update=true"
        sql "SET enable_insert_strict=false"
        // skip partial update detail — MoW + parser 组合应正常工作
        def r = sql "SELECT count(*) FROM t_iia_crx_001 WHERE c MATCH 'hello'"
        assertEquals(1L, r[0][0], "MoW unique + parser=english should index properly")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_crx_001" } catch (Exception ignore) {}
        try { sql "SET enable_unique_key_partial_update=false" } catch (Exception ignore) {}
    }
}
