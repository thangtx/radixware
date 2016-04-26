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
 
#include "generated\org_radixware_kernel_utils_wia_AbstractComObjectStream.h"
#include "jniUtils.h"

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_AbstractComObjectStream_readObject(JNIEnv *env, jclass, jlong pointer, jstring iidAsStr)
{
	IStream *pStream = reinterpret_cast<IStream *>(pointer);
	IID iid;
	writeJstringToGuid(env, iidAsStr, &iid);
	VOID *pVoid = NULL;
	REFIID riid = iid;
	HRESULT result = CoGetInterfaceAndReleaseStream(pStream, riid, &pVoid);
	if (checkResult(result, env, false))
	{
		return pVoid==NULL ? 0 : reinterpret_cast<jlong>(pVoid);
	}
	else
	{
		return 0;
	}
}