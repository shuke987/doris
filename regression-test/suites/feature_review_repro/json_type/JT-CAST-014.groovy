// JT-CAST-014: jsonb_string_as_string session — 当前 branch 不存在此 var (Doc/Mode SEV)
suite("repro_jt_cast_014") {
    // System variable doesn't exist on this branch; record as Doc gap
    boolean threw = false; String err = ""
    try { sql "SET jsonb_string_as_string=true" }
    catch (Exception e) { threw = true; err = e.message ?: "" }
    // record observation; do not fail
    if (threw) {
        assertTrue(err.contains("Unknown") || err.contains("jsonb_string_as_string"),
            "JT-CAST-014: session var should either exist or fail with clear msg; observed=${err.take(120)}")
    }
}
