// IIA-CUS-006: 引用不存在的 analyzer → 拒绝（CREATE TABLE 时）
suite("repro_iia_cus_006") {
    sql "DROP TABLE IF EXISTS t_iia_cus_006"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_cus_006 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('analyzer'='nonexistent_analyzer_xyz'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
    }
    assertTrue(threw, "FE should reject reference to nonexistent analyzer")
    sql "DROP TABLE IF EXISTS t_iia_cus_006"
}
