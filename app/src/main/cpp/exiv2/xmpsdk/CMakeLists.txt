add_library(exiv2-xmp STATIC
    src/ExpatAdapter.cpp
    src/MD5.cpp
    src/ParseRDF.cpp
    src/UnicodeConversions.cpp
    src/WXMPIterator.cpp
    src/WXMPMeta.cpp
    src/WXMPUtils.cpp
    src/XML_Node.cpp
    src/XMPCore_Impl.cpp
    src/XMPIterator.cpp
    src/XMPMeta-GetSet.cpp
    src/XMPMeta-Parse.cpp
    src/XMPMeta-Serialize.cpp
    src/XMPMeta.cpp
    src/XMPUtils-FileInfo.cpp
    src/XMPUtils.cpp
    include/MD5.h
    include/TXMPIterator.hpp
    include/TXMPMeta.hpp
    include/TXMPUtils.hpp
    include/XMP_Const.h
    include/XMP_Environment.h
    include/XMP.incl_cpp
    include/XMPSDK.hpp
    include/XMP_Version.h
)

target_link_libraries(exiv2-xmp
    PRIVATE 
        expat
)

target_include_directories(exiv2-xmp
    PRIVATE 
        ./include }
)

# Prevent a denial-service-attack related to XML entity expansion
# ("billion laughs attack").
# See https://bugzilla.redhat.com/show_bug.cgi?id=888769
target_compile_definitions(exiv2-xmp PRIVATE BanAllEntityUsage=1)

target_compile_options(exiv2-xmp PRIVATE -Wno-deprecated-register)

if (BUILD_SHARED_LIBS)
    set_property(TARGET exiv2-xmp PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()
