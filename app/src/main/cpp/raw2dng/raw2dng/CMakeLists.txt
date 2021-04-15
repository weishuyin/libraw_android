# The version number.
SET(RAW2DNG_VERSION_MAJOR 1)
SET(RAW2DNG_VERSION_MINOR 2)
SET(RAW2DNG_VERSION_PATCH 2)

# configure a header file to pass some of the CMake settings
# to the source code
CONFIGURE_FILE ( "${CMAKE_CURRENT_SOURCE_DIR}/config.h.cmake"
                 "${CMAKE_CURRENT_BINARY_DIR}/config.h" )

ADD_LIBRARY( raw2dng SHARED
                ${CMAKE_CURRENT_SOURCE_DIR}/raw2dng.cpp
                ${CMAKE_CURRENT_SOURCE_DIR}/negativeProcessor.cpp
                ${CMAKE_CURRENT_SOURCE_DIR}/rawConverter.cpp
                ${CMAKE_CURRENT_SOURCE_DIR}/vendorProcessors/DNGprocessor.cpp
                ${CMAKE_CURRENT_SOURCE_DIR}/vendorProcessors/ILCE7processor.cpp
                ${CMAKE_CURRENT_SOURCE_DIR}/vendorProcessors/FujiProcessor.cpp
                ${CMAKE_CURRENT_SOURCE_DIR}/vendorProcessors/variousVendorProcessor.cpp )

TARGET_INCLUDE_DIRECTORIES(raw2dng PUBLIC ../libdng
        ../libdng/dng-sdk/source
        ./ ../)

TARGET_LINK_LIBRARIES(raw2dng dng zlib raw jpeg exiv2lib)
TARGET_COMPILE_OPTIONS( raw2dng PRIVATE -fexceptions -std=c++11 -Wno-deprecated-declarations)

