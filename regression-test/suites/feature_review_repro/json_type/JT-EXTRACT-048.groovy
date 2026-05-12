// JT-EXTRACT-048: extract_string + jsonb_string_as_string session — session 不存在
suite("repro_jt_extract_048") {
    // session var doesn't exist on this branch; extract_string just returns bare string
    def r = sql "SELECT jsonb_extract_string(CAST('{\"a\":\"hi\"}' AS JSONB), '\$.a')"
    assertEquals("hi", r[0][0].toString(), "JT-EXTRACT-048; observed=${r}")
}
