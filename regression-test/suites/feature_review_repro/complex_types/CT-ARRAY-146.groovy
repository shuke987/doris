suite("repro_ct_array_146") {
    def r = sql "SELECT array_intersect(array(NULL,1), array(NULL,2))"
    Object obs = r[0][0]
    assertTrue(obs != null, "CT-ARRAY-146: intersect with NULL no crash; observed=${r}")
}
