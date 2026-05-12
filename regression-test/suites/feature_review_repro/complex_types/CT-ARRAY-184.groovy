suite("repro_ct_array_184") {
    def r = sql "SELECT array_pushback(array(1,2), 3)"
    assertEquals("[1, 2, 3]", r[0][0].toString(), "CT-ARRAY-184: pushback; observed=${r}")
}
