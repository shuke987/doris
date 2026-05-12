suite("repro_ct_array_187") {
    def r = sql "SELECT array_pushfront(array(2,3), 1)"
    assertEquals("[1, 2, 3]", r[0][0].toString(), "CT-ARRAY-187: pushfront; observed=${r}")
}
