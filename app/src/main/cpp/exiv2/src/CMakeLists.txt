include(CMakePackageConfigHelpers)
include_directories(${CMAKE_CURRENT_BINARY_DIR})

add_library( exiv2lib
        ../include/exiv2/config.h
        ../include/exiv2/exiv2.hpp
        ../include/exiv2/rwlock.hpp
        ../include/exiv2/slice.hpp
        basicio.cpp             ../include/exiv2/basicio.hpp
        bmpimage.cpp            ../include/exiv2/bmpimage.hpp
        convert.cpp             ../include/exiv2/convert.hpp
        cr2image.cpp            ../include/exiv2/cr2image.hpp
        crwimage.cpp            ../include/exiv2/crwimage.hpp
        datasets.cpp            ../include/exiv2/datasets.hpp
        easyaccess.cpp          ../include/exiv2/easyaccess.hpp
        epsimage.cpp            ../include/exiv2/epsimage.hpp
        error.cpp               ../include/exiv2/error.hpp
        exif.cpp                ../include/exiv2/exif.hpp
        futils.cpp              ../include/exiv2/futils.hpp
        gifimage.cpp            ../include/exiv2/gifimage.hpp
        http.cpp                ../include/exiv2/http.hpp
        image.cpp               ../include/exiv2/image.hpp
        ini.cpp                 ../include/exiv2/ini.hpp
        iptc.cpp                ../include/exiv2/iptc.hpp
        jp2image.cpp            ../include/exiv2/jp2image.hpp
        jpgimage.cpp            ../include/exiv2/jpgimage.hpp
        metadatum.cpp           ../include/exiv2/metadatum.hpp
        mrwimage.cpp            ../include/exiv2/mrwimage.hpp
        orfimage.cpp            ../include/exiv2/orfimage.hpp
        pgfimage.cpp            ../include/exiv2/pgfimage.hpp
        preview.cpp             ../include/exiv2/preview.hpp
        properties.cpp          ../include/exiv2/properties.hpp
        psdimage.cpp            ../include/exiv2/psdimage.hpp
        rafimage.cpp            ../include/exiv2/rafimage.hpp
        rw2image.cpp            ../include/exiv2/rw2image.hpp
        tags.cpp                ../include/exiv2/tags.hpp
        tgaimage.cpp            ../include/exiv2/tgaimage.hpp
        tiffimage.cpp           ../include/exiv2/tiffimage.hpp
        types.cpp               ../include/exiv2/types.hpp
        value.cpp               ../include/exiv2/value.hpp
        version.cpp             ../include/exiv2/version.hpp
        webpimage.cpp           ../include/exiv2/webpimage.hpp
        xmp.cpp                 ../include/exiv2/xmp_exiv2.hpp
        xmpsidecar.cpp          ../include/exiv2/xmpsidecar.hpp
        canonmn_int.cpp         canonmn_int.hpp
        casiomn_int.cpp         casiomn_int.hpp
        cr2header_int.cpp       cr2header_int.hpp
        crwimage_int.cpp        crwimage_int.hpp
        fujimn_int.cpp          fujimn_int.hpp
        helper_functions.cpp    helper_functions.hpp
        image_int.cpp           image_int.hpp
        makernote_int.cpp       makernote_int.hpp
        minoltamn_int.cpp       minoltamn_int.hpp
        nikonmn_int.cpp         nikonmn_int.hpp
        olympusmn_int.cpp       olympusmn_int.hpp
        orfimage_int.cpp        orfimage_int.hpp
        panasonicmn_int.cpp     panasonicmn_int.hpp
        pentaxmn_int.cpp        pentaxmn_int.hpp
        rw2image_int.cpp        rw2image_int.hpp
        safe_op.hpp
        samsungmn_int.cpp       samsungmn_int.hpp
        sigmamn_int.cpp         sigmamn_int.hpp
        sonymn_int.cpp          sonymn_int.hpp
        tags_int.cpp            tags_int.hpp
        tiffcomposite_int.cpp   tiffcomposite_int.hpp
        tiffimage_int.cpp       tiffimage_int.hpp
        tiffvisitor_int.cpp     tiffvisitor_int.hpp
        tifffwd_int.hpp
        timegm.h
        unused.h
        bmffimage.cpp           ../include/exiv2/bmffimage.hpp
)

target_compile_options(exiv2lib PRIVATE -Wno-deprecated-declarations -Wno-sign-compare)

# Other library target properties
# ---------------------------------------------------------

if (${CMAKE_CXX_COMPILER_ID} STREQUAL GNU)
    # Do not check format overflows on this file, to skip a false positive warning
    set_source_files_properties(value.cpp PROPERTIES COMPILE_FLAGS -Wno-format-overflow)
endif()

set_target_properties( exiv2lib PROPERTIES
    VERSION       ${PROJECT_VERSION}
    SOVERSION     ${PROJECT_VERSION_MINOR}
    OUTPUT_NAME   exiv2
    PDB_OUTPUT_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}
    COMPILE_FLAGS ${EXTRA_COMPILE_FLAGS}
)

set_target_properties( exiv2lib PROPERTIES
    POSITION_INDEPENDENT_CODE ON
    COMPILE_DEFINITIONS exiv2lib_EXPORTS
)

target_include_directories(exiv2lib PRIVATE 
    ../xmpsdk/include
)

if (EXIV2_ENABLE_XMP)
    target_link_libraries(exiv2lib PRIVATE exiv2-xmp)
endif()

# Use include/exiv2 to build libary code
target_include_directories(exiv2lib PUBLIC
        ../
        ../include/exiv2
        ../include
        ../xmpsdk/include
)
