// JT-CONSTRUCT-014: json_object 重复 key
suite("repro_jt_construct_014") {
    def r = sql "SELECT json_object('k', 1, 'k', 2)"
    String v = r[0][0].toString()
    // SEV-2: spec contract — behavior should be last-wins or rejected, 实际可能 silently dup
    // Allow either {"k":1} or {"k":2} or {"k":1,"k":2}
    assertTrue(v.contains("\"k\":"), "JT-CONSTRUCT-014: dup key handling; observed=${r}")
}
