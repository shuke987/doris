// CT-ARRAY-022: AGGREGATE + ARRAY + MAX/MIN
suite("repro_ct_array_022") {
    sql "DROP TABLE IF EXISTS t_ct_array_022"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_022 (id INT, a ARRAY<INT> MAX)
            AGGREGATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_array_022" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-ARRAY-022: AGGREGATE ARRAY MAX must reject; threw=${threw} err=${err}")
}
