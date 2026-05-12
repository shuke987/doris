// JT-COMPAT-001: MySQL JSON_TYPE 兼容
suite("repro_jt_compat_001") {
    // Standard MySQL: JSON_TYPE returns uppercase like "OBJECT". Doris jsonb_type returns lowercase.
    def r1 = sql "SELECT jsonb_type(CAST('{\"a\":1}' AS JSONB), '\$')"
    String v = r1[0][0].toString()
    // observed: lowercase "object"
    assertEquals("object", v.toLowerCase(),
        "JT-COMPAT-001: jsonb_type returns lowercase, MySQL uses uppercase (Doc divergence); observed=${r1}")
}
