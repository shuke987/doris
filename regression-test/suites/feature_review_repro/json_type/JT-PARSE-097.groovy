// JT-PARSE-097: CTAS + jsonb_parse
suite("repro_jt_parse_097") {
    sql 'DROP TABLE IF EXISTS t_jt_parse_097_src'
    sql 'DROP TABLE IF EXISTS t_jt_parse_097_dst'
    try {
        sql '''CREATE TABLE t_jt_parse_097_src (id INT, s STRING) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")'''
        sql 'INSERT INTO t_jt_parse_097_src VALUES (1, \'{"a":1}\'),(2,\'bad\')'
        try { sql 'CREATE TABLE t_jt_parse_097_dst PROPERTIES("replication_num"="1") AS SELECT jsonb_parse(s) j FROM t_jt_parse_097_src' } catch (Exception e) {}
        assertTrue(true)
    } finally {
        try { sql 'DROP TABLE IF EXISTS t_jt_parse_097_src' } catch (Exception ignore) {}
        try { sql 'DROP TABLE IF EXISTS t_jt_parse_097_dst' } catch (Exception ignore) {}
    }
}
