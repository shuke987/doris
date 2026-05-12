// CT-ARRAY-029: ARRAY 列 DEFAULT '[]' - 行为断言 (spec 仅 NULL)
suite("repro_ct_array_029") {
    sql "DROP TABLE IF EXISTS t_ct_array_029"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_029 (id INT, a ARRAY<INT> DEFAULT '[]')
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_array_029" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-ARRAY-029: ARRAY DEFAULT '[]' should be rejected (spec: only NULL); threw=${threw} err=${err}")
}
