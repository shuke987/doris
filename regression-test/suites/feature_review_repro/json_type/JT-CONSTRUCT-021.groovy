// JT-CONSTRUCT-021: json_object 嵌套
suite("repro_jt_construct_021") {
    def r = sql "SELECT json_object('outer', json_object('inner', 1))"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"outer\":") && v.contains("\"inner\":1"),
        "JT-CONSTRUCT-021; observed=${r}")
}
