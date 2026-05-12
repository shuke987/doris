// JT-CAST-010: 隐式 cast：`INSERT JSONB_COL VALUES 'abc'`
suite("repro_jt_cast_010") {
    sql 'DROP TABLE IF EXISTS t_jt_cast_010'
    try {
        sql '''CREATE TABLE t_jt_cast_010 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        boolean threw=false
        try { sql 'INSERT INTO t_jt_cast_010 VALUES (1, \'abc\')' } catch (Exception e) { threw=true }
        def r = sql 'SELECT count(*) FROM t_jt_cast_010'
        // behavior under-specified; either throw or insert NULL
        assertTrue(threw || r[0][0].toString() == '1', "JT-CAST-010; observed threw=${threw} count=${r}")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cast_010' } catch (Exception ignore) {} }
}
