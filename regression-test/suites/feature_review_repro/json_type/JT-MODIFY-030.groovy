// JT-MODIFY-030: json_remove 删 key
suite("repro_jt_modify_030") {
    def r = sql "SELECT json_remove(CAST('{\"a\":1,\"b\":2}' AS JSONB), '\$.a')"
    String v = r[0][0].toString()
    assertTrue(!v.contains("\"a\":") && v.contains("\"b\":2"),
        "JT-MODIFY-030; observed=${r}")
}
