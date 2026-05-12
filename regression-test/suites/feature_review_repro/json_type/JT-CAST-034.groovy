// JT-CAST-034: jsonb → DECIMAL
suite("repro_jt_cast_034") {
    def r = sql """SELECT CAST(CAST('1.5' AS JSONB) AS DECIMAL(10,2))"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('1.50'), "observed=${r}")
}
