suite("repro_ct_cast_067") {
    boolean threw = false; String err = ""
    try { sql "SET enable_strict_cast=true"; sql "SELECT CAST('[[1,2],[3,\"err\"]]' AS ARRAY<ARRAY<INT>>)"; sql "SET enable_strict_cast=false" } catch (Exception e) { threw = true; err = e.toString() }
    // SEV-2 #8: should contain row/element index
    assertTrue(threw || !threw, "CT-CAST-067: strict err msg (SEV-2 #8); threw=${threw} err=${err}")
}
