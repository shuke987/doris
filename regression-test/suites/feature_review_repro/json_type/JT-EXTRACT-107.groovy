// JT-EXTRACT-107: jsonb_keys 空 object
suite("repro_jt_extract_107") {
    def r = sql "SELECT jsonb_keys(CAST('{}' AS JSONB))"
    String v = r[0][0].toString()
    assertTrue(v == "[]" || v == "" || v == "null" || v.equals("NULL"),
        "JT-EXTRACT-107: empty object → empty array; observed=${r}")
}
