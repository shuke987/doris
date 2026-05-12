// JT-EXTRACT-076: isnull 对 T_Null
suite("repro_jt_extract_076") {
    def r = sql "SELECT json_extract_isnull(CAST('{\"a\":null}' AS JSONB), '\$.a')"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "1" || v == "true", "JT-EXTRACT-076; observed=${r}")
}
