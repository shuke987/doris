suite("repro_ct_struct_012") {
    sql "DROP TABLE IF EXISTS t_ct_struct_012"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_012 (id INT, s STRUCT<Aa:INT, aa:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_012" } catch (Exception ignore) {} }
    // SEV-2 #N7 fieldMap lowercased -> Aa/aa collide
    assertTrue(threw, "CT-STRUCT-012: case-insensitive dup (SEV-2 #N7); threw=${threw} err=${err}")
}
