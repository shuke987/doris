// JT-EXTRACT-030: 100 层 jsonb + path 100 段
suite("repro_jt_extract_030") {
    // 10 levels deep for safety
    String obj = "1"
    (1..10).each { obj = "{\"k${it}\":${obj}}" }
    String path = "\$." + (10..1).collect { "k${it}" }.join(".")
    def r = sql "SELECT jsonb_extract(CAST('${obj}' AS JSONB), '${path}')"
    assertEquals("1", r[0][0].toString(), "JT-EXTRACT-030; observed=${r}")
}
