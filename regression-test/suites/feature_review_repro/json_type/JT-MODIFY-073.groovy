// JT-MODIFY-073: `json_remove()` / `json_remove(j)` 0/1 参
suite("repro_jt_modify_073") {
    boolean threw0 = false; boolean threw1 = false
    try { sql 'SELECT json_remove()' } catch (Exception e) { threw0=true }
    try { sql 'SELECT json_remove(CAST(\'{\"a\":1}\' AS JSONB))' } catch (Exception e) { threw1=true }
    assertTrue(threw0 && threw1, "JT-MODIFY-073: 0/1 arg should reject")
}
