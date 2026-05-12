// JT-CROSS-057: INSERT jsonb parse fail 事务回滚
suite("repro_jt_cross_057") {
    sql 'DROP TABLE IF EXISTS t_jt_cross_057'
    try {
        sql '''CREATE TABLE t_jt_cross_057 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        boolean threw=false
        try { sql 'INSERT INTO t_jt_cross_057 VALUES (1, jsonb_parse(\'bad\'))' } catch (Exception e) { threw=true }
        def r = sql 'SELECT count(*) FROM t_jt_cross_057'
        // tx rollback should keep table empty if threw
        assertTrue(threw ? r[0][0].toString() == '0' : true, "JT-CROSS-057: rollback check; threw=${threw} count=${r}")
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cross_057' } catch (Exception ignore) {} }
}
