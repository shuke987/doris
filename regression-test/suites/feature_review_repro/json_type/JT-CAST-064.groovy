// JT-CAST-064: json_array 混合 implicit cast
suite("repro_jt_cast_064") {
    def r = sql "SELECT json_array(1, 'a', 3.14, true, NULL)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\"") && v.contains("true") && v.contains("null"),
        "JT-CAST-064; observed=${r}")
}
