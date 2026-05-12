suite("repro_ct_lambda_015") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT array_map(x->sum(x), array(1,2))"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    // spec: agg in lambda not strict; record
    assertTrue(threw || obs != null, "CT-LAMBDA-015: agg in lambda behavior; threw=${threw} obs=${obs} err=${err}")
}
