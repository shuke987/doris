// JT-COMPAT-029: JSON_LENGTH NULL
suite("repro_jt_compat_029") {
    def r = sql "SELECT JSON_LENGTH(NULL)"
    assertEquals(null, r[0][0], "JT-COMPAT-029; observed=${r}")
}
