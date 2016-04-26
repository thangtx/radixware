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
#include "generated\org_radixware_kernel_utils_wia_properties_WiaPropertyStorage.h"
#include "Wia.h"
#include "Wiadef.h"
#include "Objbase.h"
#include "Sti.h"
#include "jniUtils.h"

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_properties_WiaPropertyStorage_writeMultiple(JNIEnv *env, jclass classObj, jlong pointer, jobjectArray props)
{
	int numArr = env->GetArrayLength(props);
	if (numArr>0)
	{
		jclass propClassId = env->FindClass(JAVA_CLASS_PATH"/properties/ComProperty");
		jmethodID mthIsPropId = env->GetMethodID(propClassId, "propSpecIsPropId", "()Z");
		jmethodID mthGetPropSpecId = env->GetMethodID(propClassId, "getPropSpecId", "()J");
		jmethodID mthGetPropSpecName = env->GetMethodID(propClassId, "getPropSpecName", "()Ljava/lang/String;");
		jmethodID mthGetValTypeCode = env->GetMethodID(propClassId, "getValTypeCode", "()I");
		jmethodID mthGetNativeValue = env->GetMethodID(propClassId, "getNativeValue", "()Ljava/lang/Object;");
		PROPSPEC* arrPropSpec = new PROPSPEC[numArr];
		memset(arrPropSpec,0,sizeof(PROPSPEC) * numArr);
		PROPVARIANT* arrPropValues = new PROPVARIANT[numArr];		
		for (int i = 0; i < numArr; i++)
		{
			jobject item = env->GetObjectArrayElement(props, i);
			if (env->CallBooleanMethod(item,mthIsPropId)==JNI_TRUE)
			{
				arrPropSpec[i].ulKind = PRSPEC_PROPID;
				arrPropSpec[i].propid = (PROPID)env->CallLongMethod(item,mthGetPropSpecId);
			}
			else
			{
				arrPropSpec[i].ulKind = PRSPEC_LPWSTR;
				jstring propSpecName = (jstring)env->CallObjectMethod(item,mthGetPropSpecName);
				arrPropSpec[i].lpwstr = jstring2BSTR(env, propSpecName);
			}			
			jobject jvalue = env->CallObjectMethod(item, mthGetNativeValue);
			if (jvalue==NULL)
			{
				arrPropValues[i].vt = VT_EMPTY;
			}
			else
			{
				jint typeCode = env->CallIntMethod(item, mthGetValTypeCode);
				switch(typeCode)
				{
				case VT_I4:
					{
						arrPropValues[i].vt = VT_I4;
						jclass intClassId = env->FindClass("java/lang/Integer");
						jmethodID mthIntValue = env->GetMethodID(intClassId, "intValue", "()I");
						jint propValue = env->CallIntMethod(jvalue, mthIntValue);
						arrPropValues[i].lVal = propValue;
					}
					break;
				case VT_R4:
					{
						arrPropValues[i].vt = VT_R4;
						jclass floatClassId = env->FindClass("java/lang/Float");
						jmethodID mthFloatValue = env->GetMethodID(floatClassId, "floatValue", "()F");
						jfloat propValue = env->CallFloatMethod(jvalue, mthFloatValue);
						arrPropValues[i].fltVal = propValue;
					}
					break;
				case VT_BSTR:
					{
						arrPropValues[i].vt = VT_BSTR;
						arrPropValues[i].bstrVal = jstring2BSTR(env,(jstring)jvalue);
					}
					break;
				case VT_UI2:
					{
						arrPropValues[i].vt = VT_UI2;
						jclass charClassId = env->FindClass("java/lang/Character");
						jmethodID mthCharValue = env->GetMethodID(charClassId, "charValue", "()C");
						jchar propValue = env->CallCharMethod(jvalue, mthCharValue);
						arrPropValues[i].uiVal = propValue;
					}
					break;					
				case VT_CLSID:
					{
						arrPropValues[i].vt = VT_CLSID;
						arrPropValues[i].puuid = new GUID();
						if (!writeJstringToGuid(env,(jstring)jvalue,arrPropValues[i].puuid)){
							arrPropValues[i].vt = VT_EMPTY;
							delete arrPropValues[i].puuid;
							arrPropValues[i].puuid = NULL;
							continue;
						}
					}
					break;
				default:
					{
						arrPropValues[i].vt = VT_EMPTY;
					}
				}
			}				
		}
		IWiaPropertyStorage *pWiaPropertyStorage = reinterpret_cast<IWiaPropertyStorage *>(pointer);
		HRESULT hr = pWiaPropertyStorage->WriteMultiple( numArr, arrPropSpec, arrPropValues, WIA_IPA_FIRST );
		for (int i = 0; i < numArr; i++)
		{
			if ( arrPropSpec[i].ulKind == PRSPEC_LPWSTR && NULL!=arrPropSpec[i].lpwstr )
			{
				SysFreeString(arrPropSpec[i].lpwstr);
				arrPropSpec[i].lpwstr = NULL;
			}
			switch (arrPropValues[i].vt)
			{
			case VT_BSTR:
				{
					if (arrPropValues[i].bstrVal)
					{
						SysFreeString(arrPropValues[i].bstrVal);
						arrPropValues[i].bstrVal = NULL;
					}
				}
			case VT_CLSID:
				{
					if (arrPropValues[i].puuid)
					{
						delete arrPropValues[i].puuid;
						arrPropValues[i].puuid = NULL;
					}
				}
			}
		}
		delete []arrPropSpec;
		delete []arrPropValues;
		if ( FAILED(hr) ){
			throwComException(env, hr, false);
		}
	}
}

JNIEXPORT jboolean JNICALL Java_org_radixware_kernel_utils_wia_properties_WiaPropertyStorage_readMultiple(JNIEnv *env, jclass, jlong pointer, jobjectArray props)
{
	int numArr = env->GetArrayLength(props);
	if (numArr>0)
	{
		jclass propClassId = env->FindClass(JAVA_CLASS_PATH"/properties/ComProperty");
		jmethodID mthIsPropId = env->GetMethodID(propClassId, "propSpecIsPropId", "()Z");
		jmethodID mthGetPropSpecId = env->GetMethodID(propClassId, "getPropSpecId", "()J");
		jmethodID mthGetPropSpecName = env->GetMethodID(propClassId, "getPropSpecName", "()Ljava/lang/String;");
		jmethodID mthSetNativeValue = env->GetMethodID(propClassId, "setNativeValue", "(Ljava/lang/Object;)V");
		PROPSPEC* arrPropSpec = new PROPSPEC[numArr];
		memset(arrPropSpec,0,sizeof(PROPSPEC) * numArr);
		PROPVARIANT* arrPropValues = new PROPVARIANT[numArr];		
		for (int i = 0; i < numArr; i++)
		{
			jobject item = env->GetObjectArrayElement(props, i);
			if (env->CallBooleanMethod(item,mthIsPropId)==JNI_TRUE)
			{
				arrPropSpec[i].ulKind = PRSPEC_PROPID;
				arrPropSpec[i].propid = (PROPID)env->CallLongMethod(item,mthGetPropSpecId);
			}
			else
			{
				arrPropSpec[i].ulKind = PRSPEC_LPWSTR;
				jstring propSpecName = (jstring)env->CallObjectMethod(item,mthGetPropSpecName);
				arrPropSpec[i].lpwstr = jstring2BSTR(env, propSpecName);
			}
			PropVariantInit( &arrPropValues[i] );
		}
		IWiaPropertyStorage *pWiaPropertyStorage = reinterpret_cast<IWiaPropertyStorage *>(pointer);
		HRESULT hr = pWiaPropertyStorage->ReadMultiple(numArr, arrPropSpec, arrPropValues);
		for (int i = 0; i < numArr; i++)
		{
			if ( arrPropSpec[i].ulKind == PRSPEC_LPWSTR && NULL!=arrPropSpec[i].lpwstr )
			{
				SysFreeString(arrPropSpec[i].lpwstr);
				arrPropSpec[i].lpwstr = NULL;
			}			
		}		
		delete []arrPropSpec;
		jboolean result = S_FALSE;
		if ( hr!=S_FALSE && checkResult(hr, env, false))
		{
			for (int i = 0; i < numArr; i++)
			{
				jobject item = env->GetObjectArrayElement(props, i);
				jobject value = NULL;
				switch(arrPropValues[i].vt)
				{
				case VT_I4:
					{
						jclass intClassId = env->FindClass("java/lang/Integer");
						jmethodID mthIntValue = env->GetStaticMethodID(intClassId, "valueOf", "(I)Ljava/lang/Integer;");
						value = env->CallStaticObjectMethod(intClassId, mthIntValue, arrPropValues[i].lVal);							
					}
					break;
				case VT_R4:
					{
						jclass floatClassId = env->FindClass("java/lang/Float");
						jmethodID mthFloatValue = env->GetStaticMethodID(floatClassId, "valueOf", "(F)Ljava/lang/Float;");
						value = env->CallStaticObjectMethod(floatClassId, mthFloatValue, arrPropValues[i].fltVal);
					}
					break;
				case VT_BSTR:
					{					
						value = arrPropValues[i].bstrVal ? (jobject)BSTR2jstring(env, arrPropValues[i].bstrVal) : NULL;
					}
					break;
				case VT_UI2:
					{
						jclass intClassId = env->FindClass("java/lang/Integer");
						jmethodID mthIntValue = env->GetStaticMethodID(intClassId, "valueOf", "(I)Ljava/lang/Integer;");
						value = env->CallStaticObjectMethod(intClassId, mthIntValue, arrPropValues[i].uiVal);
					}
					break;
				case VT_CLSID:
					{
						value = arrPropValues[i].puuid ? (jobject) guid2jstring(env, *arrPropValues[i].puuid) : NULL;
					}
					break;
				case VT_NULL:
					{
						value = NULL;
					}
				default:
					{
						PropVariantClear( &arrPropValues[i] );
						continue;
					}
				}
				PropVariantClear( &arrPropValues[i] );
				env->CallVoidMethod(item, mthSetNativeValue, value);
				result = JNI_TRUE;
			}
			delete []arrPropValues;
		}
		return result;
	}
	return JNI_FALSE;
}

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_properties_WiaPropertyStorage_getProperties(JNIEnv *env, jclass, jlong pointer)
{
	IWiaPropertyStorage *pWiaPropertyStorage = reinterpret_cast<IWiaPropertyStorage *>(pointer);
	IEnumSTATPROPSTG *penum = NULL;
	HRESULT hr = pWiaPropertyStorage->Enum(&penum);
	return checkResult(hr, env, false) ? reinterpret_cast<jlong>(penum) : 0;
}

JNIEXPORT jint JNICALL Java_org_radixware_kernel_utils_wia_properties_WiaPropertyStorage_getCount(JNIEnv *env, jclass, jlong pointer)
{
	IWiaPropertyStorage *pWiaPropertyStorage = reinterpret_cast<IWiaPropertyStorage *>(pointer);
	ULONG count = 0;
	checkResult(pWiaPropertyStorage->GetCount(&count), env, false);
	return (jint)count;
}

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_properties_WiaPropertyStorage_commit(JNIEnv *env, jclass, jlong pointer, jint flag)
{
	IWiaPropertyStorage *pWiaPropertyStorage = reinterpret_cast<IWiaPropertyStorage *>(pointer);
	checkResult(pWiaPropertyStorage->Commit((DWORD)flag), env, false);
}

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_properties_WiaPropertyStorage_revert(JNIEnv *env, jclass, jlong pointer)
{
	IWiaPropertyStorage *pWiaPropertyStorage = reinterpret_cast<IWiaPropertyStorage *>(pointer);
	checkResult(pWiaPropertyStorage->Revert(), env, false);
}