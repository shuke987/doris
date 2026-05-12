// JT-COMPAT-034: json_extract function lower-case
suite("repro_jt_compat_034") {
    def r = sql "SELECT json_extract(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    assertEquals("1", r[0][0].toString(), "JT-COMPAT-034; observed=${r}")
}
