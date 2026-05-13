// IIA-PRS-021: parser=chinese 标点处理
suite("repro_iia_prs_021") {
    def r = sql """SELECT tokenize('你好，世界！doris', '"parser"="chinese"')"""
    String s = r[0][0].toString()
    assertTrue(s.contains('"token": "你"') || s.contains('"token": "你好"'),
               "chinese should produce token for 你/你好; got=${s}")
    assertTrue(s.contains('"token": "世界"') || s.contains('"token": "世"'),
               "chinese should produce token for 世界/世; got=${s}")
    assertTrue(s.contains('"token": "doris"'),
               "chinese should still produce english tokens; got=${s}")
    // 中文标点不应是 token
    assertFalse(s.contains('"token": "，"') || s.contains('"token": "！"'),
                "chinese should NOT tokenize punctuation; got=${s}")
}
