add_library(jpeg SHARED
        jaricom.c jcomapi.c jutils.c jerror.c jmemmgr.c jmemnobs.c
        jcapimin.c jcapistd.c jcarith.c jctrans.c jcparam.c
        jdatadst.c jcinit.c jcmaster.c jcmarker.c jcmainct.c jcprepct.c
        jccoefct.c jccolor.c jcsample.c jchuff.c jcdctmgr.c jfdctfst.c
        jfdctflt.c jfdctint.c
        jdapimin.c jdapistd.c jdarith.c jdtrans.c jdatasrc.c
        jdmaster.c jdinput.c jdmarker.c jdhuff.c jdmainct.c
        jdcoefct.c jdpostct.c jddctmgr.c jidctfst.c jidctflt.c
        jidctint.c jdsample.c jdcolor.c jquant1.c jquant2.c jdmerge.c
        )

target_include_directories(jpeg PUBLIC ./)
