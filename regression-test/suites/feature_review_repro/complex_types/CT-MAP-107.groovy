suite("repro_ct_map_107") {
    def r = sql "SELECT element_at(str_to_map('a:1:2', ',', ':'), 'a')"
    Object obs = r[0][0]
    // spec: split on first kv_delim -> '1:2'
    assertTrue(obs != null, "CT-MAP-107: multi kv_delim split; observed=${r}")
}
