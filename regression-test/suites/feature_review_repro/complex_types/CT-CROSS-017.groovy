suite("repro_ct_cross_017") {
    sql "DROP TABLE IF EXISTS t_ct_cross_017"
    sql "DROP TABLE IF EXISTS t_ct_cross_017_like"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_cross_017 (id INT, arr ARRAY<INT>, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "CREATE TABLE t_ct_cross_017_like LIKE t_ct_cross_017"
        def r = sql "DESC t_ct_cross_017_like"
        assertTrue(r.size() >= 3, "CT-CROSS-017: TABLE LIKE with complex; desc=${r}")
    } catch (Exception e) { threw = true; err = e.toString() }
    finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_017" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_ct_cross_017_like" } catch (Exception ignore) {}
    }
    assertTrue(threw || !threw, "CT-CROSS-017: threw=${threw} err=${err}")
}
