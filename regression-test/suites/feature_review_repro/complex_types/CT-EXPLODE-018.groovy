suite("repro_ct_explode_018") {
    boolean threw = false; int n = -1; String err = ""
    try {
        def r = sql "SELECT count(*) FROM (SELECT 1 a) t LATERAL VIEW explode_numbers(0) tmp AS x"
        n = (r[0][0] as Number).intValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || n == 0, "CT-EXPLODE-018: 0 rows; threw=${threw} n=${n} err=${err}")
}
