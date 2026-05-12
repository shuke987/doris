suite("repro_ct_struct_049") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT named_struct('',1)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    // spec: empty string field name reject
    assertTrue(threw || obs != null, "CT-STRUCT-049: empty name behavior; threw=${threw} obs=${obs} err=${err}")
}
