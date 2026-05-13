// CT-LAMBDA-018 (HARD RULE): array_filter mask shorter than data should NOT silently truncate
// SEV-2 #N5: array_filter(array(1,2,3,4), array(true,true)) — mask len 2 < data len 4
// Spec correct: 应 (a) 报错 "mask length must match data" 或 (b) 返 NULL（清晰信号）
// 当前 4.1: silent 截断到 mask 长度 → tokens [1,2] 而非 4-row 数据应有的处理
suite("repro_ct_lambda_018") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_filter(array(1,2,3,4), array(true,true))"
        obs = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    // 期望（spec correct）：reject 或返 NULL；不应 silent 返截断结果
    assertTrue(threw || obs == null,
        "CT-LAMBDA-018 (SEV-2 #N5): array_filter mask < data MUST reject or return NULL (not silent truncate); threw=${threw} obs=${obs} err=${err}")
}
