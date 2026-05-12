// CT-ARRAY-011: ARRAY 嵌套深度 1000 层 → FE 早拒绝 / 不 OOM
suite("repro_ct_array_011") {
    sql "DROP TABLE IF EXISTS t_ct_array_011"
    StringBuilder open = new StringBuilder()
    StringBuilder close = new StringBuilder()
    for (int i = 0; i < 1000; i++) { open.append("ARRAY<"); close.append(">") }
    String typeStr = open.toString() + "INT" + close.toString()
    boolean threw = false
    String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_011 (id INT, a ${typeStr})
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) {
        threw = true; err = e.toString()
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_011" } catch (Exception ignore) {}
    }
    assertTrue(threw, "CT-ARRAY-011: 1000-level nested ARRAY FE early reject; threw=${threw} err=${err}")
}
