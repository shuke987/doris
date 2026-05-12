suite("repro_ct_array_220") {
    def r = sql "SELECT array_cum_sum(array(1,2,3))"
    String obs = r[0][0].toString()
    assertTrue(obs.contains("1") && obs.contains("3") && obs.contains("6"), "CT-ARRAY-220: cum_sum [1,3,6]; observed=${r}")
}
