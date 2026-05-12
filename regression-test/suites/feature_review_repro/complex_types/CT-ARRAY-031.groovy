// CT-ARRAY-031: ARRAY 列名大小写
suite("repro_ct_array_031") {
    sql "DROP TABLE IF EXISTS t_ct_array_031"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_031 (a ARRAY<INT>, A ARRAY<INT>, id INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_array_031" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-ARRAY-031: same-name (case-insensitive) ARRAY columns must conflict; threw=${threw} err=${err}")
}
