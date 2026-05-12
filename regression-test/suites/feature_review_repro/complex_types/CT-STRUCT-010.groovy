suite("repro_ct_struct_010") {
    sql "DROP TABLE IF EXISTS t_ct_struct_010"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_010 (id INT, s STRUCT<a:INT, a:ARRAY<INT>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_010" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-STRUCT-010: dup scalar+complex (SEV-1 #N3); threw=${threw} err=${err}")
}
