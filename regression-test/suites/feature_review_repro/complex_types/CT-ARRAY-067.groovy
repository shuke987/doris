// CT-ARRAY-067: array_range(1, 5)
suite("repro_ct_array_067") {
    def r = sql "SELECT array_size(array_range(1, 5))"
    assertEquals(4L, (r[0][0] as Number).longValue(), "CT-ARRAY-067: array_range(1, 5); observed=${r}")
}
