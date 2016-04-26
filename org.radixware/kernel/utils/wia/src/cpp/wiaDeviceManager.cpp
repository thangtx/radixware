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
#include "generated\org_radixware_kernel_utils_wia_WiaDeviceManager.h"
#include "jniUtils.h"
#include "Wia.h"

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaDeviceManager_createWiaDevMgr(JNIEnv *env, jclass)
{
	IWiaDevMgr *pWiaDevMgr = NULL;
	HRESULT hr = CoCreateInstance(CLSID_WiaDevMgr, NULL, CLSCTX_LOCAL_SERVER, IID_IWiaDevMgr, reinterpret_cast<void**>(&pWiaDevMgr));
	if (checkResult(hr, env, false))
	{
		return pWiaDevMgr==NULL ? 0 : reinterpret_cast<jlong>(pWiaDevMgr);
	}
	else
	{
		return NULL;
	}
}

JNIEXPORT jobject JNICALL Java_org_radixware_kernel_utils_wia_WiaDeviceManager_selectDevice(JNIEnv * env, jclass, jlong pointer, jlong hwndPointer, jint deviceType, jboolean showDialogForcedly)
{
    IWiaDevMgr *pWiaDevMgr = reinterpret_cast<IWiaDevMgr *>(pointer);
	LONG lFlags;
	if (showDialogForcedly)
	{
		lFlags = WIA_SELECT_DEVICE_NODEFAULT;
	}
	else
	{
		lFlags = 0;
	}
	IWiaItem *pWiaRootItem = NULL;
	BSTR pbstrDeviceID = NULL;
	HWND hwnd = reinterpret_cast<HWND>(hwndPointer);
	HRESULT hr = pWiaDevMgr->SelectDeviceDlg(hwnd, (LONG)deviceType, lFlags, &pbstrDeviceID, &pWiaRootItem);	
	if (checkResult(hr, env, false))
	{
		jstring jstrDeviceID = NULL;
		if (NULL!=pbstrDeviceID)
		{
			jstrDeviceID = BSTR2jstring(env, pbstrDeviceID);
			SysFreeString(pbstrDeviceID);
		}
		jlong jRootItemPointer = reinterpret_cast<jlong>(pWiaRootItem);
		
		jclass clsDescription = env->FindClass(JAVA_CLASS_PATH"/WiaDeviceDescription");
		jmethodID initDescription = env->GetMethodID(clsDescription, "<init>", "(Ljava/lang/String;J)V");
		
		return env->NewObject(clsDescription, initDescription, jstrDeviceID, jRootItemPointer); 
	}
	else
	{
		return NULL;
	}    
}

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaDeviceManager_getDevices(JNIEnv *env, jclass, jlong pointer)
{
	IWiaDevMgr *pWiaDevMgr = reinterpret_cast<IWiaDevMgr *>(pointer);
	IEnumWIA_DEV_INFO *pIEnumWiaDevInfo = NULL;
	HRESULT hr = pWiaDevMgr->EnumDeviceInfo(WIA_DEVINFO_ENUM_LOCAL, &pIEnumWiaDevInfo);
	checkResult(hr, env, false);
	return reinterpret_cast<jlong>(pIEnumWiaDevInfo);
}

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaDeviceManager_createDevice(JNIEnv *env, jclass, jlong pointer, jstring deviceId)
{
	IWiaDevMgr *pWiaDevMgr = reinterpret_cast<IWiaDevMgr *>(pointer);
	BSTR bstrDeviceID = jstring2BSTR(env, deviceId);
	IWiaItem *pWiaItemRoot = NULL;
	HRESULT hr = pWiaDevMgr->CreateDevice(bstrDeviceID, &pWiaItemRoot);	
	if (NULL!=bstrDeviceID)
	{
		SysFreeString(bstrDeviceID);
	}
	checkResult(hr, env, false);
	return pWiaItemRoot == NULL ? 0 : reinterpret_cast<jlong>(pWiaItemRoot);
}

JNIEXPORT jstring JNICALL Java_org_radixware_kernel_utils_wia_WiaDeviceManager_getImageDialog(JNIEnv *env, jclass, jlong pointer, jlong hwndPointer, jint devType, jint dialogFlags, jlong imgIntents, jlong rootItemPointer, jstring filePath, jstring formatGuidAsStr)
{
	IWiaDevMgr *pWiaDevMgr = reinterpret_cast<IWiaDevMgr *>(pointer);
	HWND hwnd = reinterpret_cast<HWND>(hwndPointer);
	IWiaItem *pWiaItemRoot = rootItemPointer==0 ? NULL : reinterpret_cast<IWiaItem *>(rootItemPointer);
	BSTR bstrFilePath = jstring2BSTR(env, filePath);
	GUID imageFormat = IID_NULL;
	if (NULL!=formatGuidAsStr)
	{
		writeJstringToGuid(env, formatGuidAsStr, &imageFormat);
	}
	HRESULT hr = 
		pWiaDevMgr->GetImageDlg(hwnd, (LONG) devType, (LONG) dialogFlags, (LONG) imgIntents, pWiaItemRoot, bstrFilePath, &imageFormat);
	SysFreeString(bstrFilePath);
	if (checkResult(hr, env, false))
	{
		return guid2jstring(env, imageFormat);
	}
	else
	{
		return NULL;
	}
}