suite("repro_ct_struct_062") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT struct_element(named_struct('a',1), 'missing')"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-STRUCT-062: missing field reject; threw=${threw} obs=${obs} err=${err}")
}
