suite("repro_ct_array_222") {
    def r = sql "SELECT array_difference(array(1,3,6))"
    String obs = r[0][0].toString()
    // spec: [NULL,2,3] or [0,2,3]
    assertTrue(obs.contains("2") && obs.contains("3"), "CT-ARRAY-222: difference [_,2,3]; observed=${r}")
}
