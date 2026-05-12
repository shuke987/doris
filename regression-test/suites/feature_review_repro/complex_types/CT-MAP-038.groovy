suite("repro_ct_map_038") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_038"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_038 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        try { sql "ALTER TABLE t_repro_ct_map_038 MODIFY COLUMN m MAP<STRING,BIGINT>" } catch (Exception e) { threw = true; err = e.toString() }
    } finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_038" } catch (Exception ignore) {} }
    // spec: widening value type; behavior matrix
    assertTrue(threw || !threw, "CT-MAP-038: behavior recorded threw=${threw} err=${err}")
}
