// JT-PATH-015: $.a.*
suite("repro_jt_path_015") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":{\"x\":1,\"y\":2}}' AS JSONB), '\$.a.*')"
    String v = r[0][0]?.toString() ?: ""
    assertTrue(v.contains("1") && v.contains("2"), "JT-PATH-015; observed=${r}")
}
