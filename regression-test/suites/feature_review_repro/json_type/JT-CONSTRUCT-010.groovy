// JT-CONSTRUCT-010: json_object basic
suite("repro_jt_construct_010") {
    def r = sql "SELECT json_object('k', 1, 'k2', 'v')"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"k\":1") && v.contains("\"k2\":\"v\""),
        "JT-CONSTRUCT-010; observed=${r}")
}
