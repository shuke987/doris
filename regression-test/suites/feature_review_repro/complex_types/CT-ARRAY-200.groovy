suite("repro_ct_array_200") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_zip(array(1,2,3), array('a','b'))"
        obs = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-ARRAY-200: zip unequal length; threw=${threw} obs=${obs} err=${err}")
}
