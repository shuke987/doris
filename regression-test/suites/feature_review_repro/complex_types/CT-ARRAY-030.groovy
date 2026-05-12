// CT-ARRAY-030: ARRAY 列 NOT NULL + INSERT NULL
suite("repro_ct_array_030") {
    sql "DROP TABLE IF EXISTS t_ct_array_030"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_030 (id INT, a ARRAY<INT> NOT NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        try {
            sql "INSERT INTO t_ct_array_030 VALUES (1, NULL)"
        } catch (Exception e) { threw = true; err = e.toString() }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_030" } catch (Exception ignore) {}
    }
    assertTrue(threw, "CT-ARRAY-030: NOT NULL ARRAY column + INSERT NULL must reject; threw=${threw} err=${err}")
}
