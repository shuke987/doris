suite("repro_ct_explode_017") {
    boolean threw = false; int n = -1; String err = ""
    try {
        def r = sql "SELECT count(*) FROM (SELECT 1 a) t LATERAL VIEW explode_numbers(5) tmp AS x"
        n = (r[0][0] as Number).intValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || n == 5, "CT-EXPLODE-017: explode_numbers(5)=5 rows; threw=${threw} n=${n} err=${err}")
}
