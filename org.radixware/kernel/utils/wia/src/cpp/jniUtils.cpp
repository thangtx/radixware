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
 
#include "stdafx.h"
#include "jniUtils.h"
#include "windows.h"
#include "stdio.h" 
 
void throwComException(JNIEnv *env, HRESULT hr, bool getLastError)
{
	jclass excClass = env->FindClass("org/radixware/kernel/utils/wia/ComException");
	jmethodID mthInit = env->GetMethodID(excClass, "<init>", "(IILjava/lang/String;)V");
	jobject jException;
	if (getLastError)
	{
		DWORD errorCode = GetLastError();
		if (errorCode==0)
		{
			jException =  env->NewObject(excClass, mthInit, (jint)hr, 0, NULL);
		}
		else
		{
			LPVOID lpMsgBuf = 0;
		    FormatMessage(
		        FORMAT_MESSAGE_ALLOCATE_BUFFER | 
		        FORMAT_MESSAGE_FROM_SYSTEM |
		        FORMAT_MESSAGE_IGNORE_INSERTS,
		        NULL,
		        errorCode,
		        0,
		        (LPTSTR) &lpMsgBuf,
		        0, NULL );
			if (lpMsgBuf)
			{
				jstring message = LPTSTR2jstring(env, (LPTSTR) &lpMsgBuf);
				LocalFree(lpMsgBuf);
				jException =  env->NewObject(excClass, mthInit, (jint)hr, errorCode, message);				
			}
			else
			{
				jException =  env->NewObject(excClass, mthInit, (jint)hr, errorCode, NULL);
			}
		}
	}
	else
	{
		jException =  env->NewObject(excClass, mthInit, (jint)hr, 0, NULL);
	}
	env->Throw( (jthrowable)jException );
}
