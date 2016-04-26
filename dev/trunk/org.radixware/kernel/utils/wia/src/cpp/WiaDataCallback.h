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
#include "Wia.h"
#include "WTypes.h"
#include <jni.h>

class WiaDataCallback : public IWiaDataCallback
{
private:
	JNIEnv *m_jvmEnvironment;
	jobject m_javaInstance;
	jmethodID m_javaDataCallbackMethodId;
	jmethodID m_javaHeaderCallbackMethodId;
	
	LONG  m_cRef;               // Object reference count 
	
public:
	inline WiaDataCallback(JNIEnv *env, jobject jInstance, jmethodID header_mid, jmethodID data_mid)
		: m_jvmEnvironment(env),
		  m_javaInstance(jInstance),
          m_javaHeaderCallbackMethodId(header_mid),
		  m_javaDataCallbackMethodId(data_mid),
		  m_cRef(1)
	{
	}	
	inline ~WiaDataCallback(){};
    HRESULT CALLBACK QueryInterface( REFIID riid, void **ppvObject );	
	inline ULONG CALLBACK AddRef(){return InterlockedIncrement(&m_cRef);};
	inline ULONG CALLBACK Release()
	{
		LONG cRef = InterlockedDecrement(&m_cRef);
		if (0 == cRef)
		{
			delete this;
		}
		return cRef;	
	}
    HRESULT _stdcall BandedDataCallback(
            LONG lMessage,
            LONG lStatus,
            LONG lPercentComplete,
            LONG lOffset,
            LONG lLength,
            LONG lReserved,
            LONG lResLength,
            BYTE *pbData);	
};