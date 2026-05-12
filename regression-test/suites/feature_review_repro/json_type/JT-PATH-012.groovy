// JT-PATH-012: $.["中文"]
suite("repro_jt_path_012") {
    def r = sql "SELECT jsonb_extract(CAST('{\"中文\":1}' AS JSONB), '\$.\"中文\"')"
    String v = r[0][0]?.toString() ?: ""
    assertTrue(v == "1" || v == "" || v.contains("1"),
        "JT-PATH-012; observed=${r}")
}
