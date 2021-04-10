cmake_minimum_required(VERSION 3.10.2)
project("jni")

add_library(rawjni SHARED librawjni.cpp)
target_link_libraries(rawjni raw)

add_subdirectory(LibRaw)
