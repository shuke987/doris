// JT-EXTRACT-092: jsonb_type int64
suite("repro_jt_extract_092") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":9223372036854775807}' AS JSONB), '\$.a')"
    String t = r[0][0].toString().toLowerCase()
    assertTrue(t == "int" || t == "bigint" || t == "largeint",
        "JT-EXTRACT-092; observed=${r}")
}
