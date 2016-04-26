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
#include "generated\org_radixware_kernel_utils_wia_WiaRootItem.h"
#include "jniUtils.h"
#include "Wia.h"

JNIEXPORT jobjectArray JNICALL Java_org_radixware_kernel_utils_wia_WiaRootItem_deviceDlg(JNIEnv *env, jclass, jlong pointer, jlong hwndPointer, jint dialogFlags, jlong intentFlags)
{	
	IWiaItem *pWiaRootItem = reinterpret_cast<IWiaItem *>(pointer);
	LONG itemsCount = 0;
	HWND hwnd = reinterpret_cast<HWND>(hwndPointer);
	IWiaItem **ppWiaItems = NULL;
	HRESULT hr = pWiaRootItem->DeviceDlg(hwnd, (LONG)dialogFlags, (LONG)intentFlags, &itemsCount, &ppWiaItems);
	if (checkResult(hr, env, false) && itemsCount>0)
	{
		
		jclass clsItem = env->FindClass(JAVA_CLASS_PATH"/WiaItem");
		jmethodID mthInitItem = env->GetMethodID(clsItem, "<init>", "(J)V");
		
		jmethodID initItem = env->GetMethodID(clsItem, "<init>", "(J)V");
		jobjectArray jarr = env->NewObjectArray((jsize) itemsCount, clsItem, NULL);
		for (int i=0; i<itemsCount; i++){
			jobject jItem = env->NewObject(clsItem, mthInitItem, reinterpret_cast<jlong>(ppWiaItems[i]));
			env->SetObjectArrayElement(jarr, i, jItem);
		}
		CoTaskMemFree(ppWiaItems);
		return jarr;
	}

	return NULL;
}

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaRootItem_enumDevCaps(JNIEnv *env, jclass, jlong pointer)
{
	IWiaItem *pWiaItem = reinterpret_cast<IWiaItem *>(pointer);
	IEnumWIA_DEV_CAPS *pIEnumWiaDevCaps = NULL;
	HRESULT hr = pWiaItem->EnumDeviceCapabilities(WIA_DEVICE_COMMANDS, &pIEnumWiaDevCaps);
	checkResult( hr, env, false );
	return reinterpret_cast<jlong>(pIEnumWiaDevCaps);
}

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaRootItem_deviceCommand(JNIEnv *env, jclass, jlong pointer, jstring cmdGuid)
{
	IWiaItem *pWiaItem = reinterpret_cast<IWiaItem *>(pointer);
	GUID guid;
	IWiaItem *pResultWiaItem = 0;
	writeJstringToGuid(env, cmdGuid, &guid);
	HRESULT hr = pWiaItem->DeviceCommand(0, &guid, &pResultWiaItem);
	checkResult( hr, env, false );
	return reinterpret_cast<jlong>(pResultWiaItem);
}