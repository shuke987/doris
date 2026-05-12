// JT-MODIFY-042: json_set $[last]
suite("repro_jt_modify_042") {
    def r = sql "SELECT json_set(CAST('[1,2,3]' AS JSONB), '\$[last]', 99)"
    String v = r[0][0].toString()
    assertTrue(v.contains("99"), "JT-MODIFY-042; observed=${r}")
}
