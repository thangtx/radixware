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
#include "generated\org_radixware_kernel_utils_wia_WiaItem.h"
#include "jniUtils.h"
#include "Wia.h"

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaItem_createDataTransfer(JNIEnv *env, jclass, jlong pointer)
{
    IWiaItem *pWiaItem = reinterpret_cast<IWiaItem *>(pointer);
    IWiaDataTransfer *pWiaDataTransfer = NULL;
	HRESULT hr = pWiaItem->QueryInterface( IID_IWiaDataTransfer, (void**)&pWiaDataTransfer );
	checkResult(hr, env, false);
	return reinterpret_cast<jlong>(pWiaDataTransfer);
}

JNIEXPORT jobject JNICALL Java_org_radixware_kernel_utils_wia_WiaItem_getPropertyStorage(JNIEnv *env, jclass, jlong pointer)
{
	IWiaItem *pWiaItem = reinterpret_cast<IWiaItem *>(pointer);
	IWiaPropertyStorage *pWiaPropertyStorage = NULL;
    HRESULT hr = pWiaItem->QueryInterface(IID_IWiaPropertyStorage, reinterpret_cast<void**>(&pWiaPropertyStorage));
	if (checkResult(hr, env, false))
	{
		jclass clsPropStor = env->FindClass(JAVA_CLASS_PATH"/properties/WiaPropertyStorage");
		jmethodID initPropStor = env->GetMethodID(clsPropStor, "<init>", "(J)V");	
		if (initPropStor == NULL)
		{
			return NULL;
		}
		return env->NewObject(clsPropStor, initPropStor, reinterpret_cast<jlong>(pWiaPropertyStorage));
	}
	else
	{
		return NULL;
	}
}

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaItem_enumChildItems(JNIEnv *env, jclass, jlong pointer)
{
	IWiaItem *pWiaItem = reinterpret_cast<IWiaItem *>(pointer);
	IEnumWiaItem *pIEnumWiaItem = NULL;
	checkResult(pWiaItem->EnumChildItems(&pIEnumWiaItem), env, false);
	return reinterpret_cast<jlong>(pIEnumWiaItem);
}

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaItem_getType(JNIEnv *env, jclass, jlong pointer)
{
	IWiaItem *pWiaItem = reinterpret_cast<IWiaItem *>(pointer);
	LONG itemType = 0;
	checkResult(pWiaItem->GetItemType(&itemType), env, false);
	return (jlong)itemType;
}

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_WiaItem_analyze(JNIEnv *env, jclass, jlong pointer)
{
	IWiaItem *pWiaItem = reinterpret_cast<IWiaItem *>(pointer);
	checkResult(pWiaItem->AnalyzeItem(0), env, false);
}