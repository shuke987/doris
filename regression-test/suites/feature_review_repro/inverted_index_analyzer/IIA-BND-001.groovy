// IIA-BND-001: 空字符串各 parser 行为
suite("repro_iia_bnd_001") {
    def r_none = sql """SELECT tokenize('', '"parser"="none"')"""
    assertTrue(r_none[0][0].toString().contains('"token": ""'),
               "parser=none on '' → one empty token")

    // 实测 parser=english on '' 返空字符串（无 JSON）
    def r_english = sql """SELECT tokenize('', '"parser"="english"')"""
    String s = r_english[0][0].toString()
    assertTrue(s == "" || s == "[]" || !s.contains('"token":'),
               "parser=english on '' should produce no tokens (empty string or empty array); got='${s}'")
}
