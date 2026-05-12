suite("repro_ct_struct_022") {
    sql "DROP TABLE IF EXISTS t_ct_struct_022"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_022 (id INT, s STRUCT<a:INT>)
            DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(s) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_022" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-STRUCT-022: STRUCT DISTRIBUTED BY reject; threw=${threw} err=${err}")
}
