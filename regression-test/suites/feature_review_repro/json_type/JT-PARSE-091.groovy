// JT-PARSE-091: 同一非法行 3 函数对比
suite("repro_jt_parse_091") {
    // FAIL mode should throw
    boolean fail_threw = false
    try { sql "SELECT jsonb_parse('{a')" } catch (Exception e) { fail_threw = true }
    assertTrue(fail_threw, "JT-PARSE-091: jsonb_parse (FAIL mode) should throw")
    // NULL_MODE → NULL
    def r1 = sql "SELECT jsonb_parse_error_to_null('{a')"
    assertEquals(null, r1[0][0], "JT-PARSE-091: parse_error_to_null → NULL; observed=${r1}")
    // VALUE_MODE → default '{}'
    def r2 = sql "SELECT jsonb_parse_error_to_value('{a')"
    assertEquals("{}", r2[0][0].toString(), "JT-PARSE-091: parse_error_to_value → '{}'; observed=${r2}")
}
