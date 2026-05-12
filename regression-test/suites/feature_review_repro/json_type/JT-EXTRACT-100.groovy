// JT-EXTRACT-100: jsonb_type Decimal
suite("repro_jt_extract_100") {
    def r = sql "SELECT jsonb_type(CAST(CAST(3.14 AS DECIMAL(10,2)) AS JSONB), '\$')"
    String t = r[0][0].toString().toLowerCase()
    assertTrue(t == "decimal" || t == "double" || t.contains("decimal") || t.contains("double"),
        "JT-EXTRACT-100; observed=${r}")
}
