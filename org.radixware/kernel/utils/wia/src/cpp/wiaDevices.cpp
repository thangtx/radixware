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
#include "generated\org_radixware_kernel_utils_wia_WiaDevices.h"
#include "comEnum.h"
#include "Wia.h"

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaDevices_clone(JNIEnv *env, jclass, jlong pointer)
{
	return cloneEnum<IEnumWIA_DEV_INFO>(env, pointer);
}

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaDevices_count(JNIEnv *env, jclass, jlong pointer)
{
	return getEnumItemsCount<IEnumWIA_DEV_INFO>(env, pointer);
}

JNIEXPORT jobjectArray JNICALL Java_org_radixware_kernel_utils_wia_WiaDevices_next(JNIEnv *env, jclass, jlong pointer, jint count)
{
	return getEnumItems<IEnumWIA_DEV_INFO, IWiaPropertyStorage>(env, pointer, count, JAVA_CLASS_PATH"/properties/WiaPropertyStorage");
}

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_WiaDevices_reset(JNIEnv *env, jclass, jlong pointer)
{
	resetEnum<IEnumWIA_DEV_INFO>(env, pointer);
}

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_WiaDevices_skip(JNIEnv *env, jclass, jlong pointer, jlong count)
{
	skipEnumItems<IEnumWIA_DEV_INFO>(env, pointer, count);
}