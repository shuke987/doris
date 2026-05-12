// JT-SERDE-034: rowset segment 多段 jsonb
suite("repro_jt_serde_034") {
    sql 'DROP TABLE IF EXISTS t_jt_serde_034'
    try {
        sql '''CREATE TABLE t_jt_serde_034 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        StringBuilder sb = new StringBuilder('INSERT INTO t_jt_serde_034 VALUES ')
        for (int i=0;i<200;i++) { if (i>0) sb.append(','); sb.append("(${i}, '{\"v\":").append(i).append('}\')') }
        sql sb.toString()
        def r = sql 'SELECT count(*) FROM t_jt_serde_034'
        assertEquals('200', r[0][0].toString(), "JT-SERDE-034; observed=${r}")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_serde_034' } catch (Exception ignore) {} }
}
