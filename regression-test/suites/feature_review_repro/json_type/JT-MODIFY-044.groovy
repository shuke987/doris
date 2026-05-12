// JT-MODIFY-044: json_set NULL jsonb input
suite("repro_jt_modify_044") {
    def r = sql "SELECT json_set(CAST(NULL AS JSONB), '\$.a', 1)"
    assertEquals(null, r[0][0], "JT-MODIFY-044; observed=${r}")
}
