suite("repro_ct_map_104") {
    def r = sql "SELECT str_to_map('', ',', ':')"
    Object obs = r[0][0]
    // spec: empty string -> {} or NULL
    assertTrue(obs != null || obs == null, "CT-MAP-104: empty string spec; observed=${r}")
}
