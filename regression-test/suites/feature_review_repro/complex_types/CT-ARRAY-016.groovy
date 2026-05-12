// CT-ARRAY-016: TypeDef 漏检 ARRAY of MAP<BITMAP,INT> (SEV-2 #N8)
suite("repro_ct_array_016") {
    sql "DROP TABLE IF EXISTS t_ct_array_016"
    boolean threw = false
    String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_016 (id INT, a ARRAY<MAP<BITMAP,INT>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) {
        threw = true; err = e.toString()
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_016" } catch (Exception ignore) {}
    }
    // spec: should reject because BITMAP cannot be used as MAP key nested
    assertTrue(threw, "CT-ARRAY-016: ARRAY<MAP<BITMAP,INT>> must be rejected (SEV-2 #N8 TypeDef nesting bypass); threw=${threw} err=${err}")
}
