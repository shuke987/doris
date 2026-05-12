// JT-MODIFY-051: json_remove 多 path
suite("repro_jt_modify_051") {
    def r = sql "SELECT json_remove(CAST('{\"a\":1,\"b\":2,\"c\":3}' AS JSONB), '\$.a', '\$.b')"
    String v = r[0][0].toString()
    assertTrue(!v.contains("\"a\":") && !v.contains("\"b\":") && v.contains("\"c\":3"),
        "JT-MODIFY-051; observed=${r}")
}
