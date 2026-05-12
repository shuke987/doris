// JT-CAST-055: DATETIME → JSONB — spec §3.5 not in can_cast matrix
// 实际行为：cluster rejects "cannot cast DATETIMEV2(0) to JSON" — consistent with spec
suite("repro_jt_cast_055") {
    boolean threw = false; String err = ""
    try { sql "SELECT CAST(CAST('2024-01-01 12:00:00' AS DATETIME) AS JSONB)" }
    catch (Exception e) { threw = true; err = e.message ?: "" }
    assertTrue(threw,
        "JT-CAST-055: DATETIME→JSONB should be rejected (spec §3.5); observed err=${err.take(120)}")
}
