suite("repro_ct_array_244") {
    def r = sql "SELECT array_range(5,1,-1)"
    Object obs = r[0][0]
    // spec: [5,4,3,2] size=4; bug: NULL
    assertNotNull(obs, "CT-ARRAY-244: array_range reverse must work (NEW-SEV-N12); observed=${r}")
}
