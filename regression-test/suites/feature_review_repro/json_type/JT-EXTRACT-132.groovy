// JT-EXTRACT-132: json_extract_isnull 含非法 path 应报错
suite("repro_jt_extract_132") {
    boolean threw = false
    try { sql "SELECT json_extract_isnull(CAST('{\"a\":1}' AS JSONB), 'bad_path')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-EXTRACT-132: illegal path → throw")
}
