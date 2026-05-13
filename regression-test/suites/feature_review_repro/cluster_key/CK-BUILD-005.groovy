// CK-BUILD-005: cluster key 含 VARCHAR / STRING 列（Nereids 通常允许 string/varchar 但 unique key 不允许）
suite("repro_ck_build_005") {
    sql "DROP TABLE IF EXISTS t_ck_build_005"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_ck_build_005 (id BIGINT, name VARCHAR(100), payload STRING)
            UNIQUE KEY (id)
            ORDER BY (id, name)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
    } catch (Exception e) {
        threw = true
    }
    // 实测当前 4.1 行为：Nereids 拒绝 string 类型 cluster key（与 unique key 类型限制一致）
    assertTrue(threw, "cluster key with VARCHAR should be rejected per review.md (Nereids path)")
    sql "DROP TABLE IF EXISTS t_ck_build_005"
}
