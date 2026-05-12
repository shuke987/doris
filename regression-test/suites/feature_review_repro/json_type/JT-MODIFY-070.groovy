// JT-MODIFY-070: json_set 数组 NULL 元素
suite("repro_jt_modify_070") {
    def r = sql "SELECT json_set(CAST('[1,2,3]' AS JSONB), '\$[1]', NULL)"
    String v = r[0][0].toString()
    assertTrue(v.contains("null"), "JT-MODIFY-070; observed=${r}")
}
