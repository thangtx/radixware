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
 
#include "generated\org_radixware_kernel_utils_wia_comObject.h"
#include "jniUtils.h"

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_ComObject_release(JNIEnv *env, jclass, jlong pointer)
{
	IUnknown *comPointer = reinterpret_cast<IUnknown *>(pointer);
	checkResult(comPointer->Release(), env, false);
}

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_ComObject_writeToStream(JNIEnv *env, jclass, jlong pointer, jstring iidAsStr)
{
	IUnknown *comPointer = reinterpret_cast<IUnknown *>(pointer);
	IID iid;
	writeJstringToGuid(env, iidAsStr, &iid);
	REFIID riid = iid;
	IStream *pStream = NULL;
	HRESULT result = CoMarshalInterThreadInterfaceInStream(riid, comPointer, &pStream);
	if (checkResult(result, env, false))
	{
		return pStream==NULL ? 0 : reinterpret_cast<jlong>(pStream);
	}
	else
	{
		return 0;
	}
}