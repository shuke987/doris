// JT-EXTRACT-112: keys 重复 key
suite("repro_jt_extract_112") {
    def r = sql "SELECT jsonb_keys(CAST('{\"a\":1,\"a\":2}' AS JSONB))"
    String v = r[0][0].toString()
    // observed: returns ["a","a"] — duplicate key preserved
    assertTrue(v.contains("\"a\""),
        "JT-EXTRACT-112: dup key keys handling; observed=${r}")
}
