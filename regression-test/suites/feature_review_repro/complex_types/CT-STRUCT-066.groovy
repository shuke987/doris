suite("repro_ct_struct_066") {
    def r = sql "SELECT struct_element(named_struct('中文字段', 1), '中文字段')"
    assertEquals(1, (r[0][0] as Number).intValue(), "CT-STRUCT-066: chinese field name; observed=${r}")
}
