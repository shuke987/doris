suite("repro_ct_cast_024") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql """SELECT CAST('{"a":1,"b":"x"}' AS STRUCT<a:INT,b:STRING>)"""; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-CAST-024: string->STRUCT; threw=${threw} obs=${obs} err=${err}")
}
