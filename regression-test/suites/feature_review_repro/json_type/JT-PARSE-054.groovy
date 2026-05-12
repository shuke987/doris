// JT-PARSE-054: int64 边界 2^63-1 — jsonb_type returns "bigint" for large ints
suite("repro_jt_parse_054") {
    def r = sql "SELECT jsonb_type(jsonb_parse('9223372036854775807'), '\$')"
    String t = r[0][0].toString().toLowerCase()
    // observed: distinct from small int → returns "bigint"
    assertTrue(t == "int" || t == "bigint" || t == "largeint",
        "JT-PARSE-054: 2^63-1 should be int family; observed=${r}")
}
