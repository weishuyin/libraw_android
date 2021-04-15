cmake_minimum_required(VERSION 3.10.2)
project("jni")

add_library(rawjni SHARED librawjni.cpp)
target_link_libraries(rawjni raw)

# to support canon .CR3
# in exiv2, cmake -DEXIV2_ENABLE_BMFF=On .
# in raw2dng, call Exiv2::enableBMFF();
add_library(raw2dngjni SHARED libraw2dngjni.cpp)
target_link_libraries(raw2dngjni raw2dng)

add_subdirectory(LibRaw)
add_subdirectory(expat)
add_subdirectory(exiv2)
add_subdirectory(zlib)
add_subdirectory(libjpeg)
add_subdirectory(raw2dng)
