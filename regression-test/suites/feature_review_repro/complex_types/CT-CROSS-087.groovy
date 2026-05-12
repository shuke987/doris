suite("repro_ct_cross_087") {
    // step5.2 increment: 9-function NULL matrix
    sql "DROP TABLE IF EXISTS t_ct_cross_087"
    try {
        sql """
            CREATE TABLE t_ct_cross_087 (id INT, arr ARRAY<INT>, m MAP<STRING,INT>, s STRUCT<a:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_087 VALUES (1, NULL, NULL, NULL)"
        def r = sql """SELECT
            element_at(arr, 1), array_size(arr), array_contains(arr, 1),
            map_keys(m), map_size(m), map_contains_key(m, 'a'),
            struct_element(s, 'a')
            FROM t_ct_cross_087 WHERE id=1"""
        // all should be NULL (no crash)
        for (int i = 0; i < 7; i++) {
            assertEquals(null, r[0][i], "CT-CROSS-087: idx=${i} NULL passthrough; observed=${r}")
        }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_087" } catch (Exception ignore) {}
    }
}
