suite("repro_ct_array_209") {
    def r = sql "SELECT array_avg(array(1,2,3))"
    Object obs = r[0][0]
    double v = (obs as Number).doubleValue()
    assertTrue(Math.abs(v - 2.0) < 1e-9, "CT-ARRAY-209: avg=2.0; observed=${r}")
}
