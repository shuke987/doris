// JT-EXTRACT-126: jsonb_keys 数组（非 object）应 NULL，spec
suite("repro_jt_extract_126") {
    def r = sql "SELECT jsonb_keys(CAST('[]' AS JSONB))"
    assertEquals(null, r[0][0], "JT-EXTRACT-126: keys on empty array → NULL; observed=${r}")
}
