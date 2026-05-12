// JT-MODIFY-053: json_remove 多 path 含非法
suite("repro_jt_modify_053") {
    boolean threw = false
    try { sql "SELECT json_remove(CAST('{\"a\":1}' AS JSONB), '\$.a', 'bad_path')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-MODIFY-053: illegal path in remove should throw")
}
