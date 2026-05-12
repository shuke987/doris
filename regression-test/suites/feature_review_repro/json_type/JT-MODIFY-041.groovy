// JT-MODIFY-041: json_set 数组越界 → append
suite("repro_jt_modify_041") {
    def r = sql "SELECT json_set(CAST('[1,2,3]' AS JSONB), '\$[5]', 99)"
    String v = r[0][0].toString()
    assertTrue(v.contains("99"), "JT-MODIFY-041; observed=${r}")
}
