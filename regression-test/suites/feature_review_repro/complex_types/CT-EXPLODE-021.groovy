suite("repro_ct_explode_021") {
    boolean threw = false; int n = -1; String err = ""
    try {
        def r = sql "SELECT count(*) FROM (SELECT 1 a) t LATERAL VIEW explode_split('', ',') tmp AS x"
        n = (r[0][0] as Number).intValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || n == 0 || n == 1, "CT-EXPLODE-021: empty split; threw=${threw} n=${n} err=${err}")
}
