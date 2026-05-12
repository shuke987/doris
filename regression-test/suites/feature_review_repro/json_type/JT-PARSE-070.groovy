// JT-PARSE-070: parse_error_to_null nullable 契约
suite("repro_jt_parse_070") {
    def r = sql "SELECT jsonb_parse_error_to_null('{a') IS NULL"
    assertEquals("true", r[0][0].toString().toLowerCase(),
        "JT-PARSE-070: illegal → NULL; observed=${r}")
}
