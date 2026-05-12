// JT-CAST-069: cast(127 as BIGINT) cast as JSONB
suite("repro_jt_cast_069") {
    def r = sql """SELECT json_type(CAST(CAST(127 AS BIGINT) AS JSONB), '\$')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('int'), "observed=${r}")
}
