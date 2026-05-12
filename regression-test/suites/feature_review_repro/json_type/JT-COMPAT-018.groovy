// JT-COMPAT-018: json_valid (legacy)
suite("repro_jt_compat_018") {
    def r = sql """SELECT json_valid('{}')"""
    assertTrue(['1','true'].contains(r[0][0]?.toString()), "JT-COMPAT-018; observed=${r}")
}
