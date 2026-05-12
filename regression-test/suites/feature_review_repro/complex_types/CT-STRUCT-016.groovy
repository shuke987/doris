suite("repro_ct_struct_016") {
    sql "DROP TABLE IF EXISTS t_ct_struct_016"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_016 (id INT, s STRUCT<a:BITMAP>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_016" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-STRUCT-016: STRUCT<a:BITMAP> reject; threw=${threw} err=${err}")
}
