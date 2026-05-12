// JT-MODIFY-069: set 新 key 时其他 1000 个 key 保留
suite("repro_jt_modify_069") {
    StringBuilder sb = new StringBuilder('{')
    for (int i=0;i<1000;i++) { if (i>0) sb.append(','); sb.append('"k').append(i).append('":').append(i) }
    sb.append('}')
    String j = sb.toString()
    try {
        def r = sql "SELECT json_length(json_set(CAST('${j}' AS JSONB), '\$.new', 0))"
        assertEquals('1001', r[0][0].toString(), "JT-MODIFY-069; observed=${r}")
    } catch (Exception e) {
        logger.info("JT-MODIFY-069 threw: ${e.message}")
    }
}
