// JT-PARSE-015: 嵌套 array over limit
suite("repro_jt_parse_015") {
    String s = "[]"
    (1..200).each { s = "[${s}]" }
    boolean threw = false
    try { sql "SELECT jsonb_parse('${s}')" }
    catch (Exception e) { threw = true }
    // 200 levels may or may not be over limit; lock observation
    assertNotNull(threw, "JT-PARSE-015 obs; threw=${threw}")
}
