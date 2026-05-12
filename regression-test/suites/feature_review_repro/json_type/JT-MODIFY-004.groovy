// JT-MODIFY-004: set 深嵌套 path 不存在（中间也不存在）
// Spec: MySQL 8.0 JSON_SET 行为 — path 中间层不存在时 no-op，不自动创建 chain。
//       Doris 跟齐 MySQL：json_set('{}', '$.a.b.c', 1) → '{}'（unchanged）。
suite("repro_jt_modify_004") {
    def r = sql """SELECT json_set(CAST('{}' AS JSONB), '\$.a.b.c', 1)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertEquals('{}', v, "JT-MODIFY-004: json_set path 中间不存在应 no-op (MySQL 行为); observed=${r}")
}
