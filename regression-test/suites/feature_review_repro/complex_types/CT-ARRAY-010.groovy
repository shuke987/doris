// CT-ARRAY-010: ARRAY 嵌套深度 100 层 → 拒绝
suite("repro_ct_array_010") {
    sql "DROP TABLE IF EXISTS t_ct_array_010"
    StringBuilder open = new StringBuilder()
    StringBuilder close = new StringBuilder()
    for (int i = 0; i < 100; i++) { open.append("ARRAY<"); close.append(">") }
    String typeStr = open.toString() + "INT" + close.toString()
    boolean threw = false
    String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_010 (id INT, a ${typeStr})
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) {
        threw = true; err = e.toString()
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_010" } catch (Exception ignore) {}
    }
    assertTrue(threw, "CT-ARRAY-010: 100-level nested ARRAY must be rejected; threw=${threw} err=${err}")
}
