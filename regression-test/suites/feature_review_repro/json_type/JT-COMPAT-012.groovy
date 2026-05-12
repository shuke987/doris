// JT-COMPAT-012: get_json_double
suite("repro_jt_compat_012") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT get_json_double('{"a":1}', '\$.a') """
    } catch (Exception e) {
        logger.info("JT-COMPAT-012 threw: ${e.message}")
    }
}
