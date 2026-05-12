// JT-MODIFY-043: json_set 嵌套不存在 path no-op
suite("repro_jt_modify_043") {
    def r = sql "SELECT json_set(CAST('{\"a\":1}' AS JSONB), '\$.b.c', 99)"
    String v = r[0][0].toString()
    // MySQL contract: parent must exist; observed: no-op (still {"a":1})
    assertEquals("{\"a\":1}", v,
        "JT-MODIFY-043: nested non-existent path no-op; observed=${r}")
}
