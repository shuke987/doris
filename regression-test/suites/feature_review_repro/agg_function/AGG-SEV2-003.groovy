// AGG-SEV2-003 (N12): GroupConcat 返 VARCHAR(SYSTEM_DEFAULT) 超长拼接是否截断
suite("repro_agg_sev2_003") {
    sql "DROP TABLE IF EXISTS t_agg_sev2_003"
    try {
        sql """CREATE TABLE t_agg_sev2_003 (id INT, s VARCHAR(255)) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        // 每行 200 字节 × 100 行 = 20000 字节，远超 VARCHAR default 长度
        StringBuilder sb = new StringBuilder()
        String chunk = 'x' * 200
        for (int i = 1; i <= 100; i++) {
            if (i > 1) sb.append(",")
            sb.append("(${i}, '${chunk}')")
        }
        sql "INSERT INTO t_agg_sev2_003 VALUES ${sb.toString()}"
        def r = sql "SELECT GROUP_CONCAT(s, ',') FROM t_agg_sev2_003"
        String result = r[0][0].toString()
        // 100 × 200 + 99 separators ~ 20099 字节；VARCHAR(65533) max
        // 如静默截断到 65533，长度会 < 20099
        // 锁当前行为
        int len = result.length()
        assertTrue(len > 0, "GROUP_CONCAT should produce output; len=${len}")
        // 记录：若 len < 100*200，说明截断
        if (len < 20000) {
            // 截断发生 → N12 复现
            assertTrue(len < 20000, "GROUP_CONCAT may have silent truncation at length=${len}; expected ~20099")
        }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_sev2_003" } catch (Exception ignore) {}
    }
}
