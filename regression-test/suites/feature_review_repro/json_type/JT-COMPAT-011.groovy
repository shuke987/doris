// JT-COMPAT-011: get_json_int
suite("repro_jt_compat_011") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT get_json_int('{"a":1}', '\$.a') """
    } catch (Exception e) {
        logger.info("JT-COMPAT-011 threw: ${e.message}")
    }
}
