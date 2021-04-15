add_library(expat SHARED
        lib/xmlparse.c
        lib/xmltok.c
        lib/xmlrole.c )

target_compile_definitions(expat PRIVATE HAVE_EXPAT_CONFIG_H)
target_compile_options(expat PRIVATE -Wall -Werror -Wmissing-prototypes -Wstrict-prototypes -Wno-unused-function -Wno-unused-parameter -Wno-missing-field-initializers -fexceptions -UWIN32_LEAN_AND_MEAN)

target_include_directories(expat PUBLIC ./ ./lib)
