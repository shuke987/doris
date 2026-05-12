// JT-EXTRACT-079: isnull NULL jsonb
suite("repro_jt_extract_079") {
    def r = sql "SELECT json_extract_isnull(NULL, '\$.a')"
    assertEquals(null, r[0][0], "JT-EXTRACT-079; observed=${r}")
}
