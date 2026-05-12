// JT-CONSTRUCT-002: json_array empty
suite("repro_jt_construct_002") {
    def r = sql "SELECT json_array()"
    assertEquals("[]", r[0][0].toString(), "JT-CONSTRUCT-002; observed=${r}")
}
