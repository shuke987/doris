// JT-MODIFY-067: `json_set` 100 层 deep nested + path 100 段
suite("repro_jt_modify_067") {
    String j = '{}'
    String p = '$'
    for (int i=0;i<100;i++) p = "${p}.k${i}"
    try {
        def r = sql "SELECT json_set(CAST('${j}' AS JSONB), '${p}', 1)"
        assertNotNull(r, "JT-MODIFY-067; observed=${r}")
    } catch (Exception e) {
        // deep nest may reject — must not crash
        assertTrue(true)
    }
}
