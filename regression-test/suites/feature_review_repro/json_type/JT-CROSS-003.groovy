// JT-CROSS-003: JSONB × IFNULL — SQL NULL handling
suite("repro_jt_cross_003") {
    def r = sql "SELECT IFNULL(CAST(NULL AS JSONB), CAST('{}' AS JSONB))"
    String v = r[0][0].toString()
    assertEquals("{}", v, "JT-CROSS-003; observed=${r}")
}
