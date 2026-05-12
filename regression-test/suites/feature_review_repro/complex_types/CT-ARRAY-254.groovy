suite("repro_ct_array_254") {
    sql "DROP TABLE IF EXISTS t_ct_array_254"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_254 (id INT, m MAP<STRING, ARRAY<JSONB>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_array_254" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-ARRAY-254: MAP<STRING,ARRAY<JSONB>> must reject (SEV-2 #N8); threw=${threw} err=${err}")
}
