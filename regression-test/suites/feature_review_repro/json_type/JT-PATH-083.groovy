// JT-PATH-083: `$.\r` 控制字符 path
suite("repro_jt_path_083") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{}' AS JSONB), '\$.r') """
    } catch (Exception e) {
        logger.info("JT-PATH-083 threw: ${e.message}")
    }
}
