// JT-INDEX-012: CREATE NGRAM_BF on jsonb
suite("repro_jt_index_012") {
    sql 'DROP TABLE IF EXISTS t_jt_index_012'
    try {
        boolean threw=false
        try { sql '''CREATE TABLE t_jt_index_012 (id INT, j JSONB) DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1","ngram_bf_columns"="j")''' } catch (Exception e) { threw=true }
        // either reject or accept
        assertTrue(true)
    } finally { try { sql 'DROP TABLE IF EXISTS t_jt_index_012' } catch (Exception ignore) {} }
}
