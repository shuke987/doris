// JT-CONSTRUCT-013: json_object empty
suite("repro_jt_construct_013") {
    def r = sql "SELECT json_object()"
    assertEquals("{}", r[0][0].toString(), "JT-CONSTRUCT-013; observed=${r}")
}
