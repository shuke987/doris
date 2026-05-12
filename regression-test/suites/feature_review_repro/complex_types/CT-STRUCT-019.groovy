suite("repro_ct_struct_019") {
    sql "DROP TABLE IF EXISTS t_ct_struct_019"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_019 (s STRUCT<a:INT>, id INT)
            DUPLICATE KEY(s) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_019" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-STRUCT-019: STRUCT as DUPLICATE key reject; threw=${threw} err=${err}")
}
