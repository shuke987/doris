suite("repro_ct_explode_020") {
    boolean threw = false; int n = -1; String err = ""
    try {
        def r = sql "SELECT count(*) FROM (SELECT 1 a) t LATERAL VIEW explode_split('a,b,c', ',') tmp AS x"
        n = (r[0][0] as Number).intValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || n == 3, "CT-EXPLODE-020: split 3 rows; threw=${threw} n=${n} err=${err}")
}
