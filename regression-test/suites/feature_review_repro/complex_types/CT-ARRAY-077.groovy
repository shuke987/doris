// CT-ARRAY-077: array_repeat('hello', 0)
suite("repro_ct_array_077") {
    def r = sql "SELECT array_size(array_repeat('hello', 0))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-077: array_repeat('hello', 0); observed=${r}")
}
