suite("repro_ct_index_020") {
    sql "DROP TABLE IF EXISTS t_repro_ct_index_020"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_index_020 (id INT, c STRUCT<a:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "bloom_filter_columns"="c")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_index_020" } catch (Exception ignore) {} }
    // spec: complex columns not allowed in bloom_filter
    assertTrue(threw, "CT-INDEX-020: bloom_filter STRUCT reject; threw=${threw} err=${err}")
}
