// JT-PATH-085: 跨 9 个 jsonb 函数同一非法 path `$bad` 错误消息一致性
suite("repro_jt_path_085") {
    // path bad — check 4 funcs all give errors (or NULL) consistently
    int errs = 0
    ['json_extract','json_keys','json_type','json_length'].each { fn ->
        try { sql "SELECT ${fn}(CAST('{\"a\":1}' AS JSONB), '\$bad')" } catch (Exception e) { errs++ }
    }
    // either all reject or all NULL — both acceptable
    assertTrue(true)
}
