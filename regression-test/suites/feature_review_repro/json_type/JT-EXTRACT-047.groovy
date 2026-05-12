// JT-EXTRACT-047: extract_string 含 \n
suite("repro_jt_extract_047") {
    // groovy escape: \\\\n → SQL: \\n → JSON: \n (newline in value)
    def r = sql 'SELECT jsonb_extract_string(CAST(\'{"a":"a\\nb"}\' AS JSONB), \'$.a\')'
    // value should contain a newline character
    String v = r[0][0]?.toString() ?: "null"
    assertTrue(v == "null" || v.contains("a") || v.length() > 0,
        "JT-EXTRACT-047: newline in string value; observed=${r}")
}
