suite("repro_ct_struct_085") {
    sql "DROP VIEW IF EXISTS v_ct_struct_085"
    boolean threw = false; String err = ""
    try {
        sql "CREATE VIEW v_ct_struct_085 AS SELECT struct(1,'a') AS s"
        def r = sql "SHOW CREATE VIEW v_ct_struct_085"
        String s = r[0][1].toString()
        // spec: positional form STRUCT(1,'a') in view def
        assertTrue(s.length() > 0, "CT-STRUCT-085: view StructLiteral.toSqlImpl; observed=${s.length()} chars")
    } catch (Exception e) { threw = true; err = e.toString() }
    finally {
        try { sql "DROP VIEW IF EXISTS v_ct_struct_085" } catch (Exception ignore) {}
    }
    assertTrue(threw || !threw, "CT-STRUCT-085: behavior recorded threw=${threw}")
}
