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
 
#pragma once
#include "jniUtils.h"


template<class T> inline jlong cloneEnum(JNIEnv *env, jlong pointer)
{
	T *penum = reinterpret_cast<T *>(pointer);
	T *pcopy = NULL;
	HRESULT hr = penum->Clone(&pcopy);
	return checkResult(hr, env, false) ? reinterpret_cast<jlong>(pcopy) : 0;
}

template<class T> inline jlong getEnumItemsCount(JNIEnv *env, jlong pointer)
{
	T *penum = reinterpret_cast<T *>(pointer);
	ULONG count = 0;	
	checkResult(penum->GetCount(&count), env, false);
	return (jlong)count;
}

template<class T> inline void resetEnum(JNIEnv *env, jlong pointer)
{
	T *penum = reinterpret_cast<T *>(pointer);
	checkResult(penum->Reset(), env, false);	
}

template<class T> inline void skipEnumItems(JNIEnv *env, jlong pointer, jlong count)
{
	T *penum = reinterpret_cast<T *>(pointer);
	checkResult(penum->Skip((ULONG)count), env, false);	
}

template<class ENUM_TYPE, class ITEM_TYPE> inline jobjectArray getEnumItems(JNIEnv *env, jlong pointer, jint count, char* itemClassName)
{
	ENUM_TYPE *penum = reinterpret_cast<ENUM_TYPE *>(pointer);
	ITEM_TYPE **arrItems = new ITEM_TYPE*[count];	
	ULONG fetchedCount = 0;
	HRESULT hr = penum->Next(count, arrItems, &fetchedCount);
	jobjectArray jarr = NULL;
	if (checkResult(hr, env, false))
	{
		jclass itemClassId = env->FindClass(itemClassName);
		jmethodID mthInitItem = env->GetMethodID(itemClassId, "<init>", "(J)V");
		jarr = env->NewObjectArray(fetchedCount>0 ? fetchedCount : 0, itemClassId, NULL);
		for (ULONG i=0; i<fetchedCount; i++)
		{			
			jobject jitem = env->NewObject(itemClassId, mthInitItem, reinterpret_cast<jlong>(arrItems[i]));
			env->SetObjectArrayElement(jarr, i, jitem);
		}
	}
	delete []arrItems;
	return jarr;
}