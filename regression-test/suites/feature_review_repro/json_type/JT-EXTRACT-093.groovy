// JT-EXTRACT-093: jsonb_type int128
suite("repro_jt_extract_093") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":170141183460469231731687303715884105727}' AS JSONB), '\$.a')"
    String t = r[0][0].toString().toLowerCase()
    assertTrue(t == "largeint" || t == "double" || t == "int" || t == "bigint",
        "JT-EXTRACT-093; observed=${r}")
}
