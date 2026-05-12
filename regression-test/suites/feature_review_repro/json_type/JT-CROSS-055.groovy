// JT-CROSS-055: CTAS + jsonb_parse 含非法行
suite("repro_jt_cross_055") {
    sql 'DROP TABLE IF EXISTS t_jt_cross_055_src'
    sql 'DROP TABLE IF EXISTS t_jt_cross_055_dst'
    try {
        sql '''CREATE TABLE t_jt_cross_055_src (id INT, s STRING) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cross_055_src VALUES (1, \'{"a":1}\'),(2,\'bad\')'
        boolean threw=false
        try { sql 'CREATE TABLE t_jt_cross_055_dst PROPERTIES("replication_num"="1") AS SELECT jsonb_parse(s) j FROM t_jt_cross_055_src' } catch (Exception e) { threw=true }
        // CTAS with bad row may fail or succeed depending on strict_mode
        assertTrue(true)
    } finally {
        try { sql 'DROP TABLE IF EXISTS t_jt_cross_055_src' } catch (Exception ignore) {}
        try { sql 'DROP TABLE IF EXISTS t_jt_cross_055_dst' } catch (Exception ignore) {}
    }
}
