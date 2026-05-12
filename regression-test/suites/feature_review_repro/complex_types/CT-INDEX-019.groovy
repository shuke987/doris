suite("repro_ct_index_019") {
    sql "DROP TABLE IF EXISTS t_repro_ct_index_019"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_index_019 (id INT, c MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "bloom_filter_columns"="c")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_index_019" } catch (Exception ignore) {} }
    // spec: complex columns not allowed in bloom_filter
    assertTrue(threw, "CT-INDEX-019: bloom_filter MAP reject; threw=${threw} err=${err}")
}
