// JT-COMPAT-002: MySQL JSON_EXTRACT 别名
suite("repro_jt_compat_002") {
    // MySQL: JSON_EXTRACT (uppercase). Doris: json_extract / jsonb_extract aliases.
    def r1 = sql "SELECT json_extract(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    assertEquals("1", r1[0][0].toString(), "JT-COMPAT-002 json_extract alias; observed=${r1}")
}
