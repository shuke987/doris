// JT-PATH-013: $.* wildcard object
suite("repro_jt_path_013") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":1,\"b\":2}' AS JSONB), '\$.*')"
    String v = r[0][0].toString()
    assertTrue(v.contains("1") && v.contains("2"), "JT-PATH-013; observed=${r}")
}
