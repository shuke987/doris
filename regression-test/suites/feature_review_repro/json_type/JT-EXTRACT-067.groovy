// JT-EXTRACT-067: extract_bool false
suite("repro_jt_extract_067") {
    def r = sql "SELECT jsonb_extract_bool(CAST('{\"a\":false}' AS JSONB), '\$.a')"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "false" || v == "0", "JT-EXTRACT-067; observed=${r}")
}
