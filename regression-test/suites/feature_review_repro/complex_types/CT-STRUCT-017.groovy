suite("repro_ct_struct_017") {
    sql "DROP TABLE IF EXISTS t_ct_struct_017"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_017 (id INT, s STRUCT<a:HLL>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_017" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-STRUCT-017: STRUCT<a:HLL> reject; threw=${threw} err=${err}")
}
