include(cmake/mainSetup.cmake  REQUIRED)

# options and their default values
option( BUILD_SHARED_LIBS             "Build exiv2lib as a shared library"                    ON  )
option( EXIV2_ENABLE_XMP              "Build with XMP metadata support"                       ON  )
option( EXIV2_ENABLE_EXTERNAL_XMP     "Use external version of XMP"                           OFF )
option( EXIV2_ENABLE_PNG              "Build with png support (requires libz)"                OFF  )
option( EXIV2_ENABLE_NLS              "Build native language support (requires gettext)"      OFF )
option( EXIV2_ENABLE_PRINTUCS2        "Build with Printucs2"                                  ON  )
option( EXIV2_ENABLE_LENSDATA         "Build including lens data"                             ON  )
option( EXIV2_ENABLE_VIDEO            "Build video support into library"                      OFF )
option( EXIV2_ENABLE_DYNAMIC_RUNTIME  "Use dynamic runtime (used for static libs)"            ON  )
option( EXIV2_ENABLE_WIN_UNICODE      "Use Unicode paths (wstring) on Windows"                OFF )
option( EXIV2_ENABLE_WEBREADY         "Build webready support into library"                   OFF )
option( EXIV2_ENABLE_CURL             "USE Libcurl for HttpIo (WEBREADY)"                     OFF )
option( EXIV2_ENABLE_SSH              "USE Libssh for SshIo (WEBREADY)"                       OFF )
option( EXIV2_ENABLE_BMFF             "Build with BMFF support"                               ON )

option( EXIV2_BUILD_SAMPLES           "Build sample applications"                             ON  )
option( EXIV2_BUILD_EXIV2_COMMAND     "Build exiv2 command-line executable"                   ON  )
option( EXIV2_BUILD_UNIT_TESTS        "Build unit tests"                                      OFF )
option( EXIV2_BUILD_DOC               "Add 'doc' target to generate documentation"            OFF )

# Only intended to be used by Exiv2 developers/contributors
option( EXIV2_TEAM_EXTRA_WARNINGS     "Add more sanity checks using compiler flags"           OFF )
option( EXIV2_TEAM_WARNINGS_AS_ERRORS "Treat warnings as errors"                              OFF )
option( EXIV2_TEAM_USE_SANITIZERS     "Enable ASAN and UBSAN when available"                  OFF )
option( EXIV2_TEAM_PACKAGING          "Additional stuff for generating packages"              OFF )
set(EXTRA_COMPILE_FLAGS " ")

if ( EXIV2_ENABLE_EXTERNAL_XMP )
    set(EXIV2_ENABLE_XMP OFF)
endif()

# include(cmake/findDependencies.cmake   REQUIRED)
include(cmake/compilerFlags.cmake      REQUIRED)
include(cmake/generateConfigFile.cmake REQUIRED)

include_directories(./)  # Make the exv_conf.h file visible for the full project

include(cmake/compilerFlagsExiv2.cmake REQUIRED)

add_subdirectory(xmpsdk)
add_subdirectory(src)
