// JT-EXTRACT-094: type float
suite("repro_jt_extract_094") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":1.5}' AS JSONB), '\$.a')"
    String t = r[0][0].toString().toLowerCase()
    assertTrue(t == "double" || t == "float" || t.contains("double"),
        "JT-EXTRACT-094; observed=${r}")
}
