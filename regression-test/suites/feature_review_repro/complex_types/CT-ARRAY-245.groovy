suite("repro_ct_array_245") {
    def r = sql "SELECT array_range(-1)"
    Object obs = r[0][0]
    // spec: [] or reject; bug: NULL silently
    String s = obs == null ? "null" : obs.toString()
    assertEquals("[]", s, "CT-ARRAY-245: array_range(-1) spec [] (NEW-SEV-N12 currently NULL); observed=${r}")
}
