suite("repro_ct_map_028") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_028"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_repro_ct_map_028 (m MAP<STRING,INT>, M MAP<STRING,INT>, id INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_028" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-MAP-028: case-insensitive duplicate MAP col name reject; threw=${threw} err=${err}")
}
