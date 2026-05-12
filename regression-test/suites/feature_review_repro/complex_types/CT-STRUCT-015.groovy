suite("repro_ct_struct_015") {
    sql "DROP TABLE IF EXISTS t_ct_struct_015"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_015 (id INT, s STRUCT<:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_015" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-STRUCT-015: empty field name reject; threw=${threw} err=${err}")
}
