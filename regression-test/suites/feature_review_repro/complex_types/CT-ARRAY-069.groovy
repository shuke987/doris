// CT-ARRAY-069: array_range(0)
suite("repro_ct_array_069") {
    def r = sql "SELECT array_size(array_range(0))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-069: array_range(0); observed=${r}")
}
