suite("repro_ct_struct_009") {
    sql "DROP TABLE IF EXISTS t_ct_struct_009"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_009 (id INT, s STRUCT<a:INT, a:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_009" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-STRUCT-009: dup scalar field reject; threw=${threw} err=${err}")
}
