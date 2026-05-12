suite("repro_ct_struct_008") {
    sql "DROP TABLE IF EXISTS t_ct_struct_008"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_008 (id INT, s STRUCT<a:STRUCT<a:STRUCT<a:STRUCT<a:STRUCT<a:STRUCT<a:STRUCT<a:STRUCT<a:STRUCT<a:STRUCT<a:INT>>>>>>>>>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_008" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-STRUCT-008: 10-level STRUCT reject; threw=${threw} err=${err}")
}
