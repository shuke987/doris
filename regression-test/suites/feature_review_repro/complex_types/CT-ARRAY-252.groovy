suite("repro_ct_array_252") {
    sql "DROP TABLE IF EXISTS t_ct_array_252"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_252 (id INT, a ARRAY<MAP<STRING, BITMAP>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_array_252" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-ARRAY-252: 3-level TypeDef ARRAY<MAP<STRING,BITMAP>> must reject (SEV-2 #N8); threw=${threw} err=${err}")
}
