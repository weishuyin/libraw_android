#include <jni.h>
#include <raw2dng/raw2dng.h>

extern "C" JNIEXPORT void JNICALL
Java_com_example_libraw_Raw2dng_raw2dng(JNIEnv *env, jclass cls, jstring rawFilename, jstring outFilename) {
    const char *inputFilename = env->GetStringUTFChars(rawFilename, 0);
    const char *outputFIlename = env->GetStringUTFChars(outFilename, 0);
    raw2dng(inputFilename, outputFIlename, "", false);
    env->ReleaseStringUTFChars(rawFilename, inputFilename);
    env->ReleaseStringUTFChars(outFilename, outputFIlename);
}
