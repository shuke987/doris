// CT-ARRAY-024: ARRAY 作 LIST PARTITION
suite("repro_ct_array_024") {
    sql "DROP TABLE IF EXISTS t_ct_array_024"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_024 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id)
            PARTITION BY LIST(a) (PARTITION p1 VALUES IN (('[1]')))
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_array_024" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-ARRAY-024: ARRAY cannot be LIST PARTITION column; threw=${threw} err=${err}")
}
