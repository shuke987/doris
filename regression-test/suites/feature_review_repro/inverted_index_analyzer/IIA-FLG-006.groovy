// IIA-FLG-006: parser=none + lower_case=true 行为
// 实测：parser=none 时 should_analyzer=false，char_filter 不应用，但 lower_case 是否影响？
suite("repro_iia_flg_006") {
    def r1 = sql """SELECT tokenize('Hello WORLD', '"parser"="none","lower_case"="true"')"""
    String s = r1[0][0].toString()
    // parser=none 整字段 token，lower_case 可能/不影响
    boolean has_orig = s.contains('"token": "Hello WORLD"')
    boolean has_lower = s.contains('"token": "hello world"')
    assertTrue(has_orig || has_lower,
               "parser=none + lower_case=true: should produce one full-field token (cased or lowered); got=${s}")
}
