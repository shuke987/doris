// JT-EXTRACT-121: $.a.b 中间 leg 非 object → NULL (静默 continue)
suite("repro_jt_extract_121") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":42}' AS JSONB), '\$.a.b')"
    assertEquals(null, r[0][0],
        "JT-EXTRACT-121: mid leg non-object → NULL; observed=${r}")
}
