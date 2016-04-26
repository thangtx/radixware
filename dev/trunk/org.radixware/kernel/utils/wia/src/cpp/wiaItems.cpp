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
#include "generated\org_radixware_kernel_utils_wia_WiaItems.h"
#include "comEnum.h"
#include "Wia.h"

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaItems_clone(JNIEnv *env, jclass, jlong pointer)
{
	return cloneEnum<IEnumWiaItem>(env, pointer);
}

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_WiaItems_count(JNIEnv *env, jclass, jlong pointer)
{
	return getEnumItemsCount<IEnumWiaItem>(env, pointer);
}

JNIEXPORT jobjectArray JNICALL Java_org_radixware_kernel_utils_wia_WiaItems_next(JNIEnv *env, jclass, jlong pointer, jint count)
{
	return getEnumItems<IEnumWiaItem, IWiaItem>(env, pointer, count, JAVA_CLASS_PATH"/WiaItem");
}

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_WiaItems_reset(JNIEnv *env, jclass, jlong pointer)
{
	resetEnum<IEnumWiaItem>(env, pointer);
}

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_WiaItems_skip(JNIEnv *env, jclass, jlong pointer, jlong count)
{
	skipEnumItems<IEnumWiaItem>(env, pointer, count);
}