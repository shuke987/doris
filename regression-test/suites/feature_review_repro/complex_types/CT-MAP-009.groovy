// CT-MAP-009: MAP<STRUCT<a:INT>,INT> (SEV-1 #N2 - FE depth not recursed for key)
suite("repro_ct_map_009") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_009"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_009 (id INT, m MAP<STRUCT<a:INT>,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_009" } catch (Exception ignore) {} }
    // spec: should reject (key must be primitive); SEV-1 #N2 currently allows
    assertTrue(threw, "CT-MAP-009: complex MAP key should reject (SEV-1 #N2); threw=${threw} err=${err}")
}
