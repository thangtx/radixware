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
#include "WiaDataCallback.h"
#include "jniUtils.h"

HRESULT CALLBACK WiaDataCallback::QueryInterface( REFIID riid, void **ppvObject )
{
	//
	// Validate arguments
	//
	if (NULL == ppvObject)
	{
		return E_INVALIDARG;
	}

	//
	// Return the appropriate interface
	//
	if (IsEqualIID( riid, IID_IUnknown ))
	{
		*ppvObject = static_cast<WiaDataCallback *>(this);
	}
	else if (IsEqualIID( riid, IID_IWiaDataCallback ))
	{
		*ppvObject = static_cast<WiaDataCallback *>(this);
	}
	else
	{
		*ppvObject = NULL;
		return(E_NOINTERFACE);
	}

	//
	// Increment the reference count before returning the interface.
	//
	reinterpret_cast<IUnknown*>(*ppvObject)->AddRef();
	return S_OK;
}

//
// The IWiaDataTransfer::idtGetBandedData method periodically 
// calls the IWiaDataCallback::BandedDataCallback method with
// status messages. It sends the callback method a data header
// message followed by one or more data messages to transfer 
// data. It concludes by sending a termination message.
//
HRESULT _stdcall WiaDataCallback::BandedDataCallback(
            LONG lMessage,
            LONG lStatus,
            LONG lPercentComplete,
            LONG lOffset,
            LONG lLength,
            LONG lReserved,
            LONG lResLength,
            BYTE *pbData)
{

	UNREFERENCED_PARAMETER(lReserved);
	UNREFERENCED_PARAMETER(lResLength);
        
	switch (lMessage)
	{
        case IT_MSG_DATA_HEADER:
		case IT_MSG_FILE_PREVIEW_DATA_HEADER:
            {
                //
                // The data header contains the image's final size.
                //
                PWIA_DATA_CALLBACK_HEADER pHeader = reinterpret_cast<PWIA_DATA_CALLBACK_HEADER>(pbData);
                if (pHeader)
                {
					jstring guidAsStr = guid2jstring(m_jvmEnvironment,pHeader->guidFormatID);
					jclass headerClass = m_jvmEnvironment->FindClass(JAVA_CLASS_PATH"/WiaDataCallbackHeader");
					jmethodID constructorId = m_jvmEnvironment->GetMethodID(headerClass, "<init>", "(Ljava/lang/String;JJ)V");
					jobject header = 
						m_jvmEnvironment->NewObject(headerClass, constructorId, guidAsStr, (jlong)pHeader->lBufferSize, (jlong)pHeader->lPageCount);
					jboolean result = m_jvmEnvironment->CallBooleanMethod(m_javaInstance, m_javaHeaderCallbackMethodId, header);
					if (checkException(m_jvmEnvironment))
					{
						return S_FALSE;
					}
					if (result==JNI_FALSE)
					{
						return S_FALSE;
					}
				}
            }
            break;
        case IT_MSG_DATA:
            {
				jboolean result;
                if (NULL == pbData)
                {
					result = 
						m_jvmEnvironment->CallBooleanMethod(m_javaInstance, m_javaDataCallbackMethodId, (jlong)lMessage, (jlong)lStatus, (jlong)lPercentComplete, (jlong)lOffset, (jlong)0, NULL);
                }else{
					jobject jbuffer = m_jvmEnvironment->NewDirectByteBuffer(pbData, (jlong)lLength);
					result = 
						m_jvmEnvironment->CallBooleanMethod(m_javaInstance, m_javaDataCallbackMethodId, (jlong)lMessage, (jlong)lStatus, (jlong)lPercentComplete, (jlong)lOffset, (jlong)lLength, jbuffer);
				}
				if (checkException(m_jvmEnvironment))
				{
					return S_FALSE;
				}
				if (result==JNI_FALSE){
				    return S_FALSE;
				}
            }
            break;
		case IT_MSG_STATUS:
			{
				jboolean result = m_jvmEnvironment->CallBooleanMethod(m_javaInstance, m_javaDataCallbackMethodId, (jlong)lMessage, (jlong)lStatus, (jlong)lPercentComplete, (jlong)0, (jlong)0, NULL);
				if (checkException(m_jvmEnvironment))
				{
					return S_FALSE;
				}
				if (result==JNI_FALSE)
				{
					return S_FALSE;
				}
			}
			break;
		default:
			{
				jboolean result = m_jvmEnvironment->CallBooleanMethod(m_javaInstance, m_javaDataCallbackMethodId, (jlong)lMessage, (jlong)0, (jlong)lPercentComplete, (jlong)0, (jlong)0, NULL);
				if (checkException(m_jvmEnvironment))
				{
					return S_FALSE;
				}
				if (result==JNI_FALSE)
				{
					return S_FALSE;
				}
			}
			break;
	}
	return S_OK;
}