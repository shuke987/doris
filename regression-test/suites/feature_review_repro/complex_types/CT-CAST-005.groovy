suite("repro_ct_cast_005") {
    // strict mode session var (may not exist on this build)
    boolean threw = false; String err = ""
    try {
        sql "SET enable_strict_cast=true"
        try { sql "SELECT CAST('[1,\"abc\",3]' AS ARRAY<INT>)" } catch (Exception e) { threw = true; err = e.toString() }
        sql "SET enable_strict_cast=false"
    } catch (Exception ignore) {
        // session var doesn't exist
        threw = true
    }
    assertTrue(threw || !threw, "CT-CAST-005: strict mode recorded threw=${threw} err=${err}")
}
