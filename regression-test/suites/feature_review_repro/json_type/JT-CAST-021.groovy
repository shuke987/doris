// JT-CAST-021: 嵌套对象 round-trip
suite("repro_jt_cast_021") {
    def r1 = sql "SELECT CAST(CAST('{\"a\":{\"b\":[1,2,3]}}' AS JSONB) AS STRING)"
    def r2 = sql "SELECT CAST(CAST(CAST('{\"a\":{\"b\":[1,2,3]}}' AS JSONB) AS STRING) AS JSONB)"
    assertEquals(r1[0][0].toString(), r2[0][0].toString(),
        "JT-CAST-021: round-trip; r1=${r1}, r2=${r2}")
}
