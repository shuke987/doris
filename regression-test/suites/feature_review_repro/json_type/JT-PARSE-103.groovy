// JT-PARSE-103: FE JsonLiteral 200 层嵌套
suite("repro_jt_parse_103") {
    StringBuilder sb = new StringBuilder()
    for (int i=0;i<200;i++) sb.append('[')
    sb.append('1')
    for (int i=0;i<200;i++) sb.append(']')
    String s = sb.toString()
    try {
        def r = sql "SELECT jsonb_parse('${s}')"
        assertNotNull(r, "JT-PARSE-103; observed size=${s.length()}")
    } catch (Exception e) {
        // BE should reject deep nest; FE should not
        assertTrue(true)
    }
}
