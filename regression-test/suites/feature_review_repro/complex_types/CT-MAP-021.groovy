suite("repro_ct_map_021") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_021"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_021 (id INT, m MAP<STRING,INT> SUM)
            AGGREGATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_021" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-MAP-021: AGG MAP SUM reject; threw=${threw} err=${err}")
}
