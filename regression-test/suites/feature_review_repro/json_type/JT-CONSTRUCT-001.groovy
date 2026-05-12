// JT-CONSTRUCT-001: json_array mixed
suite("repro_jt_construct_001") {
    def r = sql "SELECT json_array(1, 'a', true, NULL)"
    String v = r[0][0].toString()
    assertTrue(v.startsWith("[") && v.contains("\"a\"") && v.contains("true") && v.contains("null"),
        "JT-CONSTRUCT-001; observed=${r}")
}
