// JT-CONSTRUCT-023: object key 长 255
suite("repro_jt_construct_023") {
    String k = 'a' * 255
    def r = sql "SELECT json_object('${k}', 1)"
    assertNotNull(r[0][0], "JT-CONSTRUCT-023; observed=${r}")
}
