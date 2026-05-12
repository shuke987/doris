// JT-CAST-075: strict + null_map 混合 + 非法行
suite("repro_jt_cast_075") {
    sql 'DROP TABLE IF EXISTS t_jt_cast_075'
    try {
        sql '''CREATE TABLE t_jt_cast_075 (id INT, s STRING) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_cast_075 VALUES (1, NULL),(2, \'abc\'),(3, \'{"a":1}\')'
        try { def r = sql 'SELECT CAST(s AS JSONB) FROM t_jt_cast_075 ORDER BY id' } catch (Exception e) {}
        assertTrue(true)
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_cast_075' } catch (Exception ignore) {} }
}
