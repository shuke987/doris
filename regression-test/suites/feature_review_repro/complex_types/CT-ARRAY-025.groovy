// CT-ARRAY-025: ARRAY 作 AUTO PARTITION 表达式
suite("repro_ct_array_025") {
    sql "DROP TABLE IF EXISTS t_ct_array_025"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_025 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id)
            AUTO PARTITION BY LIST(a) ()
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_array_025" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-ARRAY-025: ARRAY cannot be AUTO PARTITION expr; threw=${threw} err=${err}")
}
