/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_keymantek_serialport_utils_SerialPort */

#ifndef _Included_com_keymantek_serialport_utils_SerialPort
#define _Included_com_keymantek_serialport_utils_SerialPort
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_keymantek_serialport_utils_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;IICI)Ljava/io/FileDescriptor;
 */
JNIEXPORT jobject JNICALL Java_com_keymantek_serialport_utils_SerialPort_open
  (JNIEnv *, jclass, jstring, jint, jint, jchar, jint, jint);

/*
 * Class:     com_keymantek_serialport_utils_SerialPort
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_keymantek_serialport_utils_SerialPort_close
  (JNIEnv *, jclass, jint);

#ifdef __cplusplus
}
#endif
#endif
