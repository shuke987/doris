// JT-PARSE-050: int16 边界 32767
// Spec: Doris JSONB 不细分 smallint/int/bigint，所有整型统一返 'int'。
//       MySQL JSON 也是统一 INTEGER。期望 'int'。
suite("repro_jt_parse_050") {
    def r = sql """SELECT json_type(jsonb_parse('32767'), '\$')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertEquals("int", v, "JT-PARSE-050: 32767 应返 int (JSONB 不细分类型); observed=${r}")
}
