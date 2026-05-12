// JT-EXTRACT-122: 非 array 节点 $[0] 返自身 / $[1] NULL
suite("repro_jt_extract_122") {
    def r1 = sql "SELECT jsonb_extract(CAST('42' AS JSONB), '\$[0]')"
    def r2 = sql "SELECT jsonb_extract(CAST('42' AS JSONB), '\$[1]')"
    assertEquals("42", r1[0][0].toString(), "JT-EXTRACT-122 [0]; observed=${r1}")
    assertEquals(null, r2[0][0], "JT-EXTRACT-122 [1]; observed=${r2}")
}
