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
#include "generated\org_radixware_kernel_utils_wia_WiaDataTransfer.h"
#include "jniUtils.h"
#include "WiaDataCallback.h"

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_WiaDataTransfer_idtGetData(JNIEnv *env, jclass, jlong thisPointer, jlong stgMediumPointer, jobject callback)
{
	jclass callbackJClass = env->FindClass(JAVA_CLASS_PATH"/WiaDataCallback");
	jmethodID mthHeaderCallback = env->GetMethodID(callbackJClass,"headerCallback","(L"JAVA_CLASS_PATH"/WiaDataCallbackHeader;)Z");
	jmethodID mthBandedDataCallback = env->GetMethodID(callbackJClass,"bandedDataCallback","(JJJJJLjava/nio/ByteBuffer;)Z");
	WiaDataCallback *pCallback = new WiaDataCallback(env, callback, mthHeaderCallback, mthBandedDataCallback);
	IWiaDataCallback *pWiaDataCallback = NULL;
    HRESULT hr = pCallback->QueryInterface( IID_IWiaDataCallback, (void**)&pWiaDataCallback );
	if (checkResult(hr, env, false))
	{
	    IWiaDataTransfer *pWiaDataTransfer = reinterpret_cast<IWiaDataTransfer *>(thisPointer);
		STGMEDIUM *pStgMedium = reinterpret_cast<STGMEDIUM *>(stgMediumPointer);
		hr = pWiaDataTransfer->idtGetData( pStgMedium, pWiaDataCallback);
		checkResult(hr, env, true);		
	}
	pCallback->Release();
}

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_WiaDataTransfer_idtGetBandedData(JNIEnv *env, jclass, jlong thisPointer, jlong bufferSize, jboolean doubleBuffer, jobject callback)
{
	jclass callbackJClass = env->FindClass(JAVA_CLASS_PATH"/WiaDataCallback");
	jmethodID mthHeaderCallback = env->GetMethodID(callbackJClass,"headerCallback","(L"JAVA_CLASS_PATH"/WiaDataCallbackHeader;)Z");
	jmethodID mthBandedDataCallback = env->GetMethodID(callbackJClass,"bandedDataCallback","(JJJJJLjava/nio/ByteBuffer;)Z");
	WiaDataCallback *pCallback = new WiaDataCallback(env, callback, mthHeaderCallback, mthBandedDataCallback);
	IWiaDataCallback *pWiaDataCallback = NULL;
    HRESULT hr = pCallback->QueryInterface( IID_IWiaDataCallback, (void**)&pWiaDataCallback );
	if (checkResult(hr, env, false))
	{
	    IWiaDataTransfer *pWiaDataTransfer = reinterpret_cast<IWiaDataTransfer *>(thisPointer);
		WIA_DATA_TRANSFER_INFO transferInfo = {0};
		transferInfo.ulSize = sizeof(WIA_DATA_TRANSFER_INFO);
		transferInfo.ulBufferSize = (ULONG)bufferSize;
		transferInfo.bDoubleBuffer = (BOOL)doubleBuffer;
		hr = pWiaDataTransfer->idtGetBandedData( &transferInfo, pWiaDataCallback );
		checkResult(hr, env, true);		
	}
	pCallback->Release();
}