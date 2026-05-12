// JT-CAST-017: T_Int128 → string 大数精度
suite("repro_jt_cast_017") {
    def r = sql """SELECT CAST(CAST('1329227995784915872903807060280344576' AS JSONB) AS STRING)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('1329227'), "observed=${r}")
}
