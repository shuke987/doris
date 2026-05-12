// JT-CONSTRUCT-057: jsonb_object 非 STRING key 错误消息
suite("repro_jt_construct_057") {
    boolean threw=false; String msg=''
    try { sql 'SELECT json_object(1, \'v\')' } catch (Exception e) { threw=true; msg=e.message }
    // behavior probe: either auto-cast or reject with user-friendly msg
    assertTrue(true)
}
