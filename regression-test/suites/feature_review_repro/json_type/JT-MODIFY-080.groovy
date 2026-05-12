// JT-MODIFY-080: NULL path + value
suite("repro_jt_modify_080") {
    def r = sql "SELECT json_set(CAST('{\"a\":1}' AS JSONB), NULL, 99)"
    // NULL path → NULL
    assertEquals(null, r[0][0],
        "JT-MODIFY-080: NULL path → NULL result; observed=${r}")
}
