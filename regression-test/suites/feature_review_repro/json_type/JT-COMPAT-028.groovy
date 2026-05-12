// JT-COMPAT-028: JSON_REMOVE MySQL
suite("repro_jt_compat_028") {
    def r = sql "SELECT JSON_REMOVE(CAST('{\"a\":1,\"b\":2}' AS JSONB), '\$.a')"
    String v = r[0][0].toString()
    assertTrue(!v.contains("\"a\":") && v.contains("\"b\":2"),
        "JT-COMPAT-028; observed=${r}")
}
