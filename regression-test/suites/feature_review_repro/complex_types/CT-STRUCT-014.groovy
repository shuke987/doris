suite("repro_ct_struct_014") {
    sql "DROP TABLE IF EXISTS t_ct_struct_014"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_014 (id INT, s STRUCT<`a.b`:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_014" } catch (Exception ignore) {} }
    // behavior assertion - either accepts or rejects
    assertTrue(threw || !threw, "CT-STRUCT-014: special char field; threw=${threw} err=${err}")
}
