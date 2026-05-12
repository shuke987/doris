// CT-ARRAY-021: AGGREGATE + ARRAY + SUM 拒绝
suite("repro_ct_array_021") {
    sql "DROP TABLE IF EXISTS t_ct_array_021"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_021 (id INT, a ARRAY<INT> SUM)
            AGGREGATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_array_021" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-ARRAY-021: AGGREGATE ARRAY SUM must reject; threw=${threw} err=${err}")
}
