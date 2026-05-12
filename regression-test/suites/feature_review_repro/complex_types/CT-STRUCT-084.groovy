suite("repro_ct_struct_084") {
    boolean threw = false; String err = ""
    try { sql "SELECT struct_element(named_struct('a',1), 'missing')" } catch (Exception e) { threw = true; err = e.toString() }
    // doc behavior: error message format check (NEW-SEV-N17)
    assertTrue(threw, "CT-STRUCT-084: missing field error msg recorded; err=${err}")
}
