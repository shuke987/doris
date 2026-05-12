// JT-PARSE-035: key 255 字节
suite("repro_jt_parse_035") {
    String k = 'a' * 255
    def r = sql "SELECT jsonb_parse('{\"${k}\":1}')"
    assertNotNull(r[0][0], "JT-PARSE-035; observed=${r}")
}
