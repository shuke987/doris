suite("repro_ct_struct_021") {
    sql "DROP TABLE IF EXISTS t_ct_struct_021"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_021 (id INT, s STRUCT<a:INT>)
            DUPLICATE KEY(id)
            PARTITION BY RANGE(s) (PARTITION p1 VALUES LESS THAN ('{1}'))
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_021" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-STRUCT-021: STRUCT PARTITION reject; threw=${threw} err=${err}")
}
