// JT-PATH-018: $**.a recursive 找所有 a key
suite("repro_jt_path_018") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":1,\"b\":{\"a\":2}}' AS JSONB), '\$**.a')"
    String v = r[0][0].toString()
    assertTrue(v.contains("1") && v.contains("2"),
        "JT-PATH-018; observed=${r}")
}
