// JT-PARSE-074: parse_error_to_null NaN → NULL
suite("repro_jt_parse_074") {
    def r = sql "SELECT jsonb_parse_error_to_null('NaN')"
    assertEquals(null, r[0][0], "JT-PARSE-074: NaN → NULL; observed=${r}")
}
