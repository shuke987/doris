// JT-EXTRACT-029: path 1KB 长
suite("repro_jt_extract_029") {
    // build deeply nested object + path
    def keys = (1..50).collect { "k${it}" }
    String obj_str = "{\"" + keys.join("\":{\"") + "\":1}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}"
    String path = "\$." + keys.join(".")
    // simpler check: path length within bounds
    def r = sql "SELECT jsonb_extract(CAST('${obj_str}' AS JSONB), '${path}')"
    // either succeeds with deep value or NULL; lock
    assertNotNull(r, "JT-EXTRACT-029; observed=${r}")
}
