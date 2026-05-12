suite("repro_ct_struct_082") {
    // Build STRUCT with 5 fields for error msg length test
    sql "DROP TABLE IF EXISTS t_ct_struct_082"
    boolean threw = false; String err = ""; int errLen = 0
    try {
        sql """
            CREATE TABLE t_ct_struct_082 (id INT, s STRUCT<f1:INT,f2:INT,f3:INT,f4:INT,f5:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        try { sql "SELECT struct_element(s, 'nonexistent') FROM t_ct_struct_082" } catch (Exception e) { threw = true; err = e.toString(); errLen = err.length() }
    } finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_082" } catch (Exception ignore) {} }
    // NEW-SEV-N17 observation: error msg should be bounded; record
    assertTrue(threw, "CT-STRUCT-082: nonexistent field error (NEW-SEV-N17 doc len=${errLen}); threw=${threw}")
}
