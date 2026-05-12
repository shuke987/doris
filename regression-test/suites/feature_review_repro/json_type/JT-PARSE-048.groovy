// JT-PARSE-048: int8 边界 127
suite("repro_jt_parse_048") {
    def r = sql "SELECT jsonb_type(jsonb_parse('127'), '\$')"
    assertEquals("int", r[0][0].toString().toLowerCase(),
        "JT-PARSE-048: int 127 type; observed=${r}")
}
