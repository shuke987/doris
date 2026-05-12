suite("repro_ct_map_016") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_016"
    boolean threw = false; String err = ""
    try {
        StringBuilder open = new StringBuilder()
        StringBuilder close = new StringBuilder()
        for (int i = 0; i < 11; i++) { open.append("MAP<STRING,"); close.append(">") }
        String tdef = open.toString() + "INT" + close.toString()
        sql """
            CREATE TABLE t_repro_ct_map_016 (id INT, m ${tdef})
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_repro_ct_map_016" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-MAP-016: 11-level MAP must reject; threw=${threw} err=${err}")
}
