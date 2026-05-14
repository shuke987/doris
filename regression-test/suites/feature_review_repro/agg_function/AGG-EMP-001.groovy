// AGG-EMP-001 (R4-5: 空表 agg 跨函数 NULL/[] 行为不一致)
// Spec: 空表上各 agg 函数 NULL/空容器返回应保持一致或文档明示差异。
// 当前 4.1:
//   COUNT(*) = 0, COUNT(v) = 0 (SQL 标准)
//   SUM/AVG/MIN/MAX/GROUP_CONCAT = NULL (SQL 标准)
//   array_agg/collect_list/collect_set = [] (Spark 风格)
// → 同为 agg，二者行为分歧；Postgres/Snowflake array_agg 返 NULL，与 Doris 不一致。
// 期望: 二者一致 (都 NULL 或都 []) 或文档明确
suite("repro_agg_emp_001") {
    sql "DROP TABLE IF EXISTS t_agg_emp_001"
    try {
        sql """
            CREATE TABLE t_agg_emp_001 (id INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        // 不 insert，空表
        def r = sql "SELECT SUM(v), array_agg(v), collect_list(v) FROM t_agg_emp_001"
        // 当前: SUM=NULL, array_agg=[], collect_list=[]
        // 锁定期望：array_agg/collect_list 在空集合上应与 SUM 一致返 NULL
        // (与 Postgres/Snowflake 一致；Spark 风格 [] 也可接受但需 doc)
        def sumNull = (r[0][0] == null)
        def arrNull = (r[0][1] == null)
        def listNull = (r[0][2] == null)
        assertTrue(sumNull == arrNull && arrNull == listNull,
            "Empty table agg consistency: SUM=null=${sumNull}, array_agg=null=${arrNull}, collect_list=null=${listNull} must agree (or doc)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_emp_001" } catch (Exception ignore) {}
    }
}
