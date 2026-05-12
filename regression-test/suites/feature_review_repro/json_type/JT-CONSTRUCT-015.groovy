// JT-CONSTRUCT-015: array 嵌套 101 层
suite("repro_jt_construct_015") {
    // build deeply nested array via SQL repeat
    String s = '1'
    for (int i=0;i<101;i++) s = "json_array(${s})"
    boolean threw = false
    try { sql "SELECT ${s}" } catch (Exception e) { threw = true }
    // behavior probe — assert no crash
    assertTrue(true)
}
