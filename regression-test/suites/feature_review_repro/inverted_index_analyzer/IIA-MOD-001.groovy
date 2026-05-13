// IIA-MOD-001: chinese parser_mode=coarse_grained 行为
suite("repro_iia_mod_001") {
    def r = sql """SELECT tokenize('北京大学', '"parser"="chinese","parser_mode"="coarse_grained"')"""
    String s = r[0][0].toString()
    // coarse 应给出 '北京大学' 长 token（如果词典中有）或 '北京' '大学'
    boolean has_long = s.contains('"token": "北京大学"') || (s.contains('"token": "北京"') && s.contains('"token": "大学"'))
    assertTrue(has_long, "coarse_grained should prefer long tokens; got=${s}")
}
