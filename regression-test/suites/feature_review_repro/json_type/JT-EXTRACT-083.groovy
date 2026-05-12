// JT-EXTRACT-083: exists_path NULL jsonb
suite("repro_jt_extract_083") {
    def r = sql "SELECT jsonb_exists_path(NULL, '\$.a')"
    assertEquals(null, r[0][0], "JT-EXTRACT-083; observed=${r}")
}
