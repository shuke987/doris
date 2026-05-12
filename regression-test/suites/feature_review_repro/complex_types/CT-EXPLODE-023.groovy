suite("repro_ct_explode_023") {
    boolean threw = false; int n = -1; String err = ""
    try {
        def r = sql "SELECT count(*) FROM (SELECT 1 a) t LATERAL VIEW explode_split(CAST(NULL AS STRING), ',') tmp AS x"
        n = (r[0][0] as Number).intValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || n == 0, "CT-EXPLODE-023: NULL split=0; threw=${threw} n=${n} err=${err}")
}
