// JT-MODIFY-040: json_set 数组索引修改
suite("repro_jt_modify_040") {
    def r = sql "SELECT json_set(CAST('[1,2,3]' AS JSONB), '\$[0]', 99)"
    String v = r[0][0].toString()
    assertTrue(v.startsWith("[99"), "JT-MODIFY-040; observed=${r}")
}
