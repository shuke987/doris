suite("repro_ct_array_171") {
    def r = sql "SELECT array_slice(array(1,2,3,4,5), -2)"
    String obs = r[0][0].toString()
    assertTrue(obs.contains("4") && obs.contains("5"), "CT-ARRAY-171: slice negative offset; observed=${r}")
}
