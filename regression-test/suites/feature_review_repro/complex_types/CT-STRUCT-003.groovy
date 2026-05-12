suite("repro_ct_struct_003") {
    sql "DROP TABLE IF EXISTS t_ct_struct_003"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_003 (id INT, s STRUCT<>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_003" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-STRUCT-003: 0-field STRUCT reject; threw=${threw} err=${err}")
}
