// JT-PARSE-052: int32 边界 2^31-1
suite("repro_jt_parse_052") {
    def r = sql "SELECT jsonb_type(jsonb_parse('2147483647'), '\$')"
    assertEquals("int", r[0][0].toString().toLowerCase(),
        "JT-PARSE-052; observed=${r}")
}
