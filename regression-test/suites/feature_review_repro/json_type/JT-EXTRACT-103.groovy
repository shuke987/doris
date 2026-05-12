// JT-EXTRACT-103: jsonb_type 大小写 (lowercase per cluster)
suite("repro_jt_extract_103") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":[]}' AS JSONB), '\$.a')"
    String v = r[0][0].toString()
    // cluster returns lowercase
    assertEquals(v.toLowerCase(), v,
        "JT-EXTRACT-103: jsonb_type returns lowercase (MySQL JSON_TYPE uppercase); observed=${r}")
}
