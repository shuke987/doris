// JT-COMPAT-022: JSON_VALID MySQL
suite("repro_jt_compat_022") {
    def r1 = sql "SELECT JSON_VALID('{\"a\":1}')"
    def r2 = sql "SELECT JSON_VALID('not json')"
    String v1 = r1[0][0].toString().toLowerCase()
    String v2 = r2[0][0].toString().toLowerCase()
    assertTrue((v1 == "1" || v1 == "true") && (v2 == "0" || v2 == "false"),
        "JT-COMPAT-022; r1=${r1}, r2=${r2}")
}
