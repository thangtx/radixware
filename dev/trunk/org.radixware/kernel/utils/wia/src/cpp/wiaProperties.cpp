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
#include "generated\org_radixware_kernel_utils_wia_properties_WiaProperties.h"
#include "comEnum.h"

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_properties_WiaProperties_clone(JNIEnv *env, jclass, jlong pointer)
{
	return cloneEnum<IEnumSTATPROPSTG>(env, pointer);
}

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_properties_WiaProperties_reset(JNIEnv *env, jclass, jlong pointer)
{
	resetEnum<IEnumSTATPROPSTG>(env, pointer);
}

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_properties_WiaProperties_skip(JNIEnv *env, jclass, jlong pointer, jlong count)
{
	skipEnumItems<IEnumSTATPROPSTG>(env, pointer, count);
}

JNIEXPORT jobjectArray JNICALL Java_org_radixware_kernel_utils_wia_properties_WiaProperties_next(JNIEnv *env, jclass, jlong selfPointer, jint count){
	IEnumSTATPROPSTG *penum = reinterpret_cast<IEnumSTATPROPSTG *>(selfPointer);
	STATPROPSTG* arrPropStg = new STATPROPSTG[count];
	memset(arrPropStg, 0, sizeof(STATPROPSTG) * count);
	ULONG actualCount = 0;
	HRESULT hr = penum->Next( (ULONG)count, arrPropStg, &actualCount );
	jobjectArray jarr = NULL;
	if (checkResult(hr, env, false))
	{
        if (actualCount>0)
		{
   	        jobject* arrJavaObjects = new jobject[actualCount];
		    char *javaClassName;
			ULONG javaArrSize=0;
		    for (ULONG i=0; i<actualCount; i++)
			{
		        switch(arrPropStg[i].vt)
			    {
			    case VT_I4:
				    {
					    javaClassName="org/radixware/kernel/utils/wia/properties/ComPropertyInt";
  				    }
				    break;
			    case VT_R4:
				    {
					    javaClassName="org/radixware/kernel/utils/wia/properties/ComPropertyFloat";
				    }
				    break;
			    case VT_BSTR:
				    {
					    javaClassName="org/radixware/kernel/utils/wia/properties/ComPropertyStr";
				    }
				    break;
			    case VT_UI2:
				    {
					    javaClassName="org/radixware/kernel/utils/wia/properties/ComPropertyUShort";
				    }
				    break;					
			    case VT_CLSID:
				    {
					    javaClassName="org/radixware/kernel/utils/wia/properties/ComPropertyUUID";					
				    }
				    break;
			    default:
				    {
				        continue;
				    }
			    }
				jclass propClassId = env->FindClass(javaClassName);
				if (arrPropStg[i].propid)
				{
					jmethodID mthInitProperty = env->GetMethodID(propClassId, "<init>", "(J)V");
					arrJavaObjects[javaArrSize] = env->NewObject(propClassId, mthInitProperty, (jlong)arrPropStg[i].propid);
					javaArrSize++;
				}
				else if (arrPropStg[i].lpwstrName)
				{
					jmethodID mthInitProperty = env->GetMethodID(propClassId, "<init>", "(Ljava/lang/String;)V");
					jstring propName = LPWSTR2jstring(env, arrPropStg[i].lpwstrName);
					arrJavaObjects[javaArrSize] = env->NewObject(propClassId, mthInitProperty, propName);
					javaArrSize++;
				}
			}
			jclass propClassId = env->FindClass(JAVA_CLASS_PATH"/properties/ComProperty");
			jarr = env->NewObjectArray(javaArrSize, propClassId, NULL);
			for (ULONG i=0; i<javaArrSize; i++)
			{
			    env->SetObjectArrayElement(jarr, i, arrJavaObjects[i]);
			}
			delete []arrJavaObjects;
			for (ULONG i=0; i<actualCount; i++)
			{
			    if ( NULL != arrPropStg[i].lpwstrName )
				{
					CoTaskMemFree( arrPropStg[i].lpwstrName );
				}
			}
		}
		else
		{
			jclass propClassId = env->FindClass(JAVA_CLASS_PATH"/properties/ComProperty");
			jarr = env->NewObjectArray(0, propClassId, NULL);
		}
	}
	delete []arrPropStg;
	return jarr;
}