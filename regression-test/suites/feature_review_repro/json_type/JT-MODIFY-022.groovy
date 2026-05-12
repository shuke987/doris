// JT-MODIFY-022: json_replace 数组索引
suite("repro_jt_modify_022") {
    def r = sql "SELECT json_replace(CAST('[1,2,3]' AS JSONB), '\$[1]', 99)"
    String v = r[0][0].toString()
    assertTrue(v.contains("99") && v.contains("1") && v.contains("3"),
        "JT-MODIFY-022; observed=${r}")
}
