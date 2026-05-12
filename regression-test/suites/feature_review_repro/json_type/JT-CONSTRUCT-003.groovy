// JT-CONSTRUCT-003: json_array 嵌套
suite("repro_jt_construct_003") {
    def r = sql "SELECT json_array(CAST('{\"a\":1}' AS JSONB), 2, 3)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":1") && v.contains("2"),
        "JT-CONSTRUCT-003; observed=${r}")
}
