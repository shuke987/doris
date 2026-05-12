// JT-MODIFY-050: json_remove 数组索引
suite("repro_jt_modify_050") {
    def r = sql "SELECT json_remove(CAST('[10,20,30]' AS JSONB), '\$[1]')"
    String v = r[0][0].toString()
    assertTrue(!v.contains("20") && v.contains("10") && v.contains("30"),
        "JT-MODIFY-050; observed=${r}")
}
