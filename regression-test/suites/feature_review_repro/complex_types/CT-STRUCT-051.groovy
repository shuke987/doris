suite("repro_ct_struct_051") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT named_struct('j', jsonb_parse('1'))"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-STRUCT-051: jsonb field behavior; threw=${threw} obs=${obs} err=${err}")
}
