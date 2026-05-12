suite("repro_ct_explode_019") {
    boolean threw = false; int n = -1; String err = ""
    try {
        def r = sql "SELECT count(*) FROM (SELECT 1 a) t LATERAL VIEW explode_numbers(-1) tmp AS x"
        n = (r[0][0] as Number).intValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || n == 0, "CT-EXPLODE-019: -1 reject/0; threw=${threw} n=${n} err=${err}")
}
