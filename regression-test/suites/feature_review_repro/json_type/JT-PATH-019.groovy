// JT-PATH-019: $**[0] recursive + array
suite("repro_jt_path_019") {
    def r = sql "SELECT jsonb_extract(CAST('{\"arr\":[1,2],\"x\":[3,4]}' AS JSONB), '\$**[0]')"
    String v = r[0][0]?.toString() ?: ""
    assertTrue(v.contains("1") || v.contains("3"),
        "JT-PATH-019; observed=${r}")
}
