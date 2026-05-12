// JT-PATH-014: $[*] array wildcard
suite("repro_jt_path_014") {
    def r = sql "SELECT jsonb_extract(CAST('[1,2,3]' AS JSONB), '\$[*]')"
    String v = r[0][0].toString()
    assertTrue(v.contains("1") && v.contains("2") && v.contains("3"),
        "JT-PATH-014; observed=${r}")
}
