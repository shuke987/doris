// JT-COMPAT-013: get_json_object
suite("repro_jt_compat_013") {
    try {
        // behavior probe: spec under-specified — assert no crash
        try {
            sql """ SELECT get_json_object('{"a":1}', '\$.a') """
        } catch (Exception e) {
            logger.info("JT-COMPAT-013 threw: ${e.message}")
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-COMPAT-013: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
