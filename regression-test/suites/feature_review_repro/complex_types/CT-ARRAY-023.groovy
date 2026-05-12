// CT-ARRAY-023: ARRAY 作 RANGE PARTITION 列
suite("repro_ct_array_023") {
    sql "DROP TABLE IF EXISTS t_ct_array_023"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_023 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id)
            PARTITION BY RANGE(a) (PARTITION p1 VALUES LESS THAN ('[100]'))
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_array_023" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-ARRAY-023: ARRAY cannot be RANGE PARTITION column; threw=${threw} err=${err}")
}
