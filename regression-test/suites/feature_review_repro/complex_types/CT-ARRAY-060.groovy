// CT-ARRAY-060: array_with_constant(-1, 'x') behavior: reject / 0 / NULL
suite("repro_ct_array_060") {
    boolean threw = false; String err = ""
    Object result = "UNKNOWN"
    try {
        def r = sql "SELECT array_with_constant(-1, 'x')"
        result = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    // spec: should reject or return [] or NULL
    assertTrue(threw || result == null || result.toString() == "[]", "CT-ARRAY-060: array_with_constant(-1) reject/NULL/[]; threw=${threw} result=${result} err=${err}")
}
