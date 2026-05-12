// CT-ARRAY-243: array_range(1,5,0) - NEW-SEV-N12 (same as 071)
suite("repro_ct_array_243") {
    boolean threw = false; Object obs = null
    try { def r = sql "SELECT array_range(1,5,0)"; obs = r[0][0] }
    catch (Exception e) { threw = true }
    // spec: should reject; bug: silently returns NULL
    assertTrue(threw, "CT-ARRAY-243: array_range step=0 must reject (NEW-SEV-N12); threw=${threw} obs=${obs}")
}
