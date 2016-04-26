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
#include "generated\org_radixware_kernel_utils_wia_WiaDeviceCapabilities.h"
#include "comEnum.h"
#include "Wia.h"

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaDeviceCapabilities_clone(JNIEnv *env, jclass, jlong pointer)
{
	return cloneEnum<IEnumWIA_DEV_CAPS>(env, pointer);
}

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaDeviceCapabilities_count(JNIEnv *env, jclass, jlong pointer)
{
	return getEnumItemsCount<IEnumWIA_DEV_CAPS>(env, pointer);
}

JNIEXPORT jobjectArray JNICALL Java_org_radixware_kernel_utils_wia_WiaDeviceCapabilities_next(JNIEnv *env, jclass, jlong pointer, jint count)
{
	IEnumWIA_DEV_CAPS *penum = reinterpret_cast<IEnumWIA_DEV_CAPS *>(pointer);
	WIA_DEV_CAP* arrDevCaps = new WIA_DEV_CAP[count];
	memset(arrDevCaps, 0, sizeof(WIA_DEV_CAP) * count);
	ULONG fetchedCount = 0;
	HRESULT hr = penum->Next(count, arrDevCaps, &fetchedCount);
	jobjectArray jarr = NULL;
	if (checkResult(hr, env, false))
	{								
		jclass wiaDevCapClassId = env->FindClass(JAVA_CLASS_PATH"/WiaDeviceCapability");
		jmethodID mthInitWiaDevCap = env->GetMethodID(wiaDevCapClassId, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
		jarr = env->NewObjectArray(fetchedCount>0 ? fetchedCount : 0, wiaDevCapClassId, NULL);
		jstring cmdGuid=NULL, name=NULL, desc=NULL, icon=NULL, cmdLine=NULL;
		jobject jDevCap=NULL;
		for (ULONG i=0; i<fetchedCount; i++)
		{
			cmdGuid = guid2jstring(env, arrDevCaps[i].guid);
			name = arrDevCaps[i].bstrName ? BSTR2jstring(env, arrDevCaps[i].bstrName) : NULL;
			desc = arrDevCaps[i].bstrDescription ? BSTR2jstring(env, arrDevCaps[i].bstrDescription) : NULL;
			icon = arrDevCaps[i].bstrIcon ? BSTR2jstring(env, arrDevCaps[i].bstrIcon) : NULL;
			cmdLine = arrDevCaps[i].bstrCommandline ? BSTR2jstring(env, arrDevCaps[i].bstrCommandline) : NULL;
			
			jDevCap = env->NewObject(wiaDevCapClassId,  mthInitWiaDevCap, cmdGuid, name, desc, icon, cmdLine);
			env->SetObjectArrayElement(jarr, i, jDevCap);
		}
	}
	for (int i=0; i<count; i++)
	{
		if ( NULL!=arrDevCaps[i].bstrName )
		{
			SysFreeString(arrDevCaps[i].bstrName);
		}
		if ( NULL!=arrDevCaps[i].bstrDescription )
		{
			SysFreeString(arrDevCaps[i].bstrDescription);
		}
		if ( NULL!=arrDevCaps[i].bstrIcon )
		{
			SysFreeString(arrDevCaps[i].bstrIcon);
		}
		if ( NULL!=arrDevCaps[i].bstrCommandline )
		{
			SysFreeString(arrDevCaps[i].bstrCommandline);
		}		
	}
	delete []arrDevCaps;
	return jarr;
}

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_WiaDeviceCapabilities_reset(JNIEnv *env, jclass, jlong pointer)
{
	resetEnum<IEnumWIA_DEV_CAPS>(env, pointer);
}

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_WiaDeviceCapabilities_skip(JNIEnv *env, jclass, jlong pointer, jlong count)
{
	skipEnumItems<IEnumWIA_DEV_CAPS>(env, pointer, count);
}
