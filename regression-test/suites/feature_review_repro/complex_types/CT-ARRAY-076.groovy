// CT-ARRAY-076: array_repeat(1, 3)
suite("repro_ct_array_076") {
    def r = sql "SELECT array_size(array_repeat(1, 3))"
    assertEquals(3L, (r[0][0] as Number).longValue(), "CT-ARRAY-076: array_repeat(1, 3); observed=${r}")
}
