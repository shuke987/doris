// JT-COMPAT-006: JSON_VALID 行为对齐
suite("repro_jt_compat_006") {
    def r = sql """SELECT json_valid('{}')"""
    assertTrue(['1','true'].contains(r[0][0]?.toString()), "JT-COMPAT-006; observed=${r}")
}
