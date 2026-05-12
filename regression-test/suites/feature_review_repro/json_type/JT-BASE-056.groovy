// JT-BASE-056: `IFNULL(j, '{}'::JSONB)`
suite("repro_jt_base_056") {
    def r = sql """SELECT IFNULL(CAST(NULL AS JSONB), CAST('{}' AS JSONB))"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('{}'), "observed=${r}")
}
