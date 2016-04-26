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
#include "generated\org_radixware_kernel_utils_wia_StgMedium.h"
#include "ObjIdl.h"
#include "Ole2.h"
#include "jniUtils.h"

class MediumStorageReleaseController : public IUnknown{

	private:
		LONG m_cRef;
	public:
		inline MediumStorageReleaseController(): m_cRef(1){};
		inline ~MediumStorageReleaseController(){};
		inline HRESULT CALLBACK QueryInterface( REFIID riid, void **ppvObject )
		{
			if (NULL == ppvObject)
			{
				return E_INVALIDARG;
			}

			if (IsEqualIID( riid, IID_IUnknown ))
			{
				*ppvObject = static_cast<MediumStorageReleaseController *>(this);
			}			
			else
			{
				*ppvObject = NULL;
				return(E_NOINTERFACE);
			}

			reinterpret_cast<IUnknown*>(*ppvObject)->AddRef();
			return S_OK;		
		}
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
};

JNIEXPORT jlong JNICALL Java_org_radixware_kernel_utils_wia_StgMedium_createFileStorage(JNIEnv *env, jclass, jstring fileName)
{
	STGMEDIUM *pStgMedium = new STGMEDIUM();
	memset(pStgMedium,0,sizeof(STGMEDIUM));
	pStgMedium->tymed = TYMED_FILE;
	if (fileName)
	{
		pStgMedium->lpszFileName = jstring2LPOLESTR(env,fileName);
	}	
	return reinterpret_cast<jlong>(pStgMedium);
};

JNIEXPORT void JNICALL Java_org_radixware_kernel_utils_wia_StgMedium_release(JNIEnv *, jclass, jlong pointer, jboolean releaseMedium, jboolean deleteFileName)
{
	STGMEDIUM *pStgMedium = reinterpret_cast<STGMEDIUM *>(pointer);
	if (NULL!=pStgMedium->lpszFileName && deleteFileName)
	{
		delete[] pStgMedium->lpszFileName;
		pStgMedium->lpszFileName = NULL;
	}
	if (NULL!=pStgMedium->lpszFileName && !releaseMedium)
	{
		pStgMedium->pUnkForRelease = new MediumStorageReleaseController();
	}
	ReleaseStgMedium(pStgMedium);
	delete pStgMedium;	
};

JNIEXPORT jstring JNICALL Java_org_radixware_kernel_utils_wia_StgMedium_getFileName (JNIEnv *env, jclass, jlong pointer)
{
	STGMEDIUM *pStgMedium = reinterpret_cast<STGMEDIUM *>(pointer);
	if (pStgMedium->tymed == TYMED_FILE && pStgMedium->lpszFileName)
	{
		return LPOLESTR2jstring(env,pStgMedium->lpszFileName);
	}
	return NULL;
};