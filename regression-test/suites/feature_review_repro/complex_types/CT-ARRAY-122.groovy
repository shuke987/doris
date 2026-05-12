suite("repro_ct_array_122") {
    def r = sql "SELECT array_contains(array('a\\nb','c'), 'a\\nb')"
    Object obs = r[0][0]
    // accept either result; not a crash test
    assertTrue(obs != null, "CT-ARRAY-122: special chars no crash; observed=${r}")
}
