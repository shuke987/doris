// JT-CAST-031: jsonb → BOOLEAN 边界 0/1
suite("repro_jt_cast_031") {
    def r0 = sql "SELECT CAST(CAST('0' AS JSONB) AS BOOLEAN)"
    def r1 = sql "SELECT CAST(CAST('1' AS JSONB) AS BOOLEAN)"
    String v0 = r0[0][0].toString().toLowerCase()
    String v1 = r1[0][0].toString().toLowerCase()
    assertTrue((v0 == "0" || v0 == "false") && (v1 == "1" || v1 == "true"),
        "JT-CAST-031; observed=[${r0}, ${r1}]")
}
