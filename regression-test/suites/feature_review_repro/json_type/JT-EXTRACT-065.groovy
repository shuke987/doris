// JT-EXTRACT-065: extract_float — function missing on this branch (spec gap; record as Doc)
suite("repro_jt_extract_065") {
    boolean threw = false; String err = ""
    try { sql "SELECT jsonb_extract_float(CAST('{\"a\":3.14}' AS JSONB), '\$.a')" }
    catch (Exception e) { threw = true; err = e.message ?: "" }
    // spec expects function exists; cluster reports "Can not found function"
    assertTrue(threw, "JT-EXTRACT-065: jsonb_extract_float observed missing; err=${err.take(120)}")
}
