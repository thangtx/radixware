/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
 
#pragma once
#include <jni.h>
#include "WTypes.h"
#include "OleAuto.h"
#include "Guiddef.h"
#include "Objbase.h"
#include "string.h"

#define JAVA_CLASS_PATH "org/radixware/kernel/utils/wia"

void throwComException(JNIEnv *env, HRESULT hr, bool getLastError);

inline bool checkResult(HRESULT hr, JNIEnv *env, bool getLastError){	
	if ( SUCCEEDED(hr) ) 
	{
		return true;
	}
	else
	{
		throwComException(env, hr, getLastError);
		return false;
	}
}

inline bool checkException(JNIEnv *env)
{
    if (env->ExceptionCheck()) 
	{
		jthrowable throbj = env->ExceptionOccurred();
		if (throbj) 
		{
			// Need to clear the exception to get FindClass() to work.
			env->ExceptionClear();
			jclass clazz = env->FindClass(JAVA_CLASS_PATH"/ComLibrary");
			jmethodID mid = env->GetStaticMethodID(clazz, "processPendingException", "(Ljava/lang/Throwable;)V");
			env->CallStaticVoidMethod(clazz, mid, throbj);
			env->DeleteLocalRef(clazz);
			env->DeleteLocalRef(throbj);
		}
        return true;
    }
    return false;
}

inline jstring BSTR2jstring(JNIEnv *env, BSTR bstr, UINT length)
{
	return env->NewString(reinterpret_cast<jchar *>(bstr), length);
}

inline jstring BSTR2jstring(JNIEnv *env, BSTR bstr)
{
	return BSTR2jstring(env, bstr, SysStringLen(bstr));
}

#if defined(_UNICODE)
inline jstring LPTSTR2jstring(JNIEnv *env, LPTSTR str)
{	
	BSTR bstr = SysAllocString(str);
	jstring result = BSTR2jstring(env,bstr);
	SysFreeString(bstr);
	return result;
}

inline jstring LPWSTR2jstring(JNIEnv *env, LPWSTR str)
{
	BSTR bstr = SysAllocString(str);
	jstring result = BSTR2jstring(env,bstr);
	SysFreeString(bstr);
	return result;
}
#endif

inline jstring LPOLESTR2jstring(JNIEnv *env, LPOLESTR str)
{
    BSTR bstr = SysAllocString(str);
    jstring result = BSTR2jstring(env,bstr);
	SysFreeString(bstr);
	return result;
}

inline BSTR jstring2BSTR(JNIEnv *env, jstring source)
{
    if (source==NULL)
	{
	   return NULL;
	}
	const jchar* jcstr = env->GetStringCritical(source, JNI_FALSE);	
	BSTR result = SysAllocStringLen((LPCOLESTR)jcstr, env->GetStringLength(source));
	env->ReleaseStringCritical(source, jcstr);
	return result;
}

inline LPOLESTR jstring2LPOLESTR(JNIEnv *env, jstring source)
{
    if (source==NULL)
	{
	   return NULL;
	}
	jsize length = env->GetStringLength(source);
	LPOLESTR str = new OLECHAR[length+1];
	env->GetStringRegion(source, 0, length, reinterpret_cast<jchar *>(str));
	str[length] = 0;
	return str;
}

inline jstring guid2jstring(JNIEnv *env, GUID &guid)
{
	LPOLESTR str = new OLECHAR[39];
	if (checkResult(StringFromGUID2(guid, str, 39),env,false))
	{
		jstring jstr = LPOLESTR2jstring(env, str);
		delete[] str;
		return jstr;
	}
	else
	{
		return NULL;
	}
}


inline boolean writeJstringToGuid(JNIEnv *env, jstring str, GUID *guid)
{ 
	BSTR bstr = jstring2BSTR(env, str);	
	HRESULT hr = CLSIDFromString(bstr, guid);
	SysFreeString(bstr);
	return checkResult(hr, env, false);
}