suite("repro_ct_map_085") {
    def r = sql "SELECT array_size(map_values(map('a',1, 'b', CAST(NULL AS INT))))"
    assertEquals(2L, (r[0][0] as Number).longValue(), "CT-MAP-085: values include NULL=2; observed=${r}")
}
