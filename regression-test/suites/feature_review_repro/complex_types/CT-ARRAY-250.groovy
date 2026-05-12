suite("repro_ct_array_250") {
    boolean threw1 = false, threw2 = false
    String err1 = "", err2 = ""
    try { sql "SELECT array_size(array_with_constant(1000000, 'x'))" } catch (Exception e) { threw1 = true; err1 = e.toString() }
    try { sql "SELECT array_with_constant(1000001, 'x')" } catch (Exception e) { threw2 = true; err2 = e.toString() }
    assertFalse(threw1, "CT-ARRAY-250a: 1M should succeed; threw=${threw1} err=${err1}")
    assertTrue(threw2, "CT-ARRAY-250b: 1M+1 should reject; threw=${threw2} err=${err2}")
}
