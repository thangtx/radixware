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
 
#include "generated\org_radixware_kernel_utils_wia_comLibrary.h"
#include "jniUtils.h"

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_ComLibrary_initialize(JNIEnv *env, jclass, jint params)
{
	checkResult(CoInitializeEx(NULL, params), env, false);
}

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_ComLibrary_uninitialize(JNIEnv *env, jclass)
{
	CoUninitialize();
}
