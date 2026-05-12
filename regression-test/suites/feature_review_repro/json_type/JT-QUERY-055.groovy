// JT-QUERY-055: json_valid scalar literals
suite("repro_jt_query_055") {
    def r1 = sql "SELECT json_valid('null')"
    def r2 = sql "SELECT json_valid('true')"
    def r3 = sql "SELECT json_valid('false')"
    String v1 = r1[0][0].toString().toLowerCase()
    String v2 = r2[0][0].toString().toLowerCase()
    String v3 = r3[0][0].toString().toLowerCase()
    assertTrue((v1 == "1" || v1 == "true") && (v2 == "1" || v2 == "true") && (v3 == "1" || v3 == "true"),
        "JT-QUERY-055: scalar literals valid; r1=${r1}, r2=${r2}, r3=${r3}")
}
