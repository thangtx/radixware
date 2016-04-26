/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.wps;

import java.util.Map;
import java.security.cert.X509Certificate;
import javax.servlet.http.HttpServletRequest;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.ssl.CertificateUtils;


public final class HttpConnectionOptions {
    
    private final boolean isSecure;
    private final X509Certificate[] certificates;
    private final String stationName;
    private final String traceProfile;
    private final EIsoLanguage language;
    private final EWebBrowserEngineType browserEngine;
    
    HttpConnectionOptions(final HttpServletRequest request){
        isSecure = request.isSecure();
        final Object cert = request.getAttribute("javax.servlet.request.X509Certificate");
        if (cert instanceof java.security.cert.X509Certificate){
            certificates = 
                new java.security.cert.X509Certificate[]{(java.security.cert.X509Certificate)cert};
        }else if (cert instanceof javax.security.cert.X509Certificate){
            certificates = 
                new java.security.cert.X509Certificate[]{CertificateUtils.convertFromJavax((javax.security.cert.X509Certificate)cert)};
        }else if (cert instanceof java.security.cert.X509Certificate[]){
            certificates = (java.security.cert.X509Certificate[])cert;
        }else if (cert instanceof javax.security.cert.X509Certificate[]){
            certificates = 
                    CertificateUtils.convertFromJavax((javax.security.cert.X509Certificate[])cert);            
        }else{
            certificates = null;
        }                
        final Map<String,String> queryParameters = HttpHeaderParser.parseQueryString(request.getQueryString());        
        final EIsoLanguage browserLanguage = 
                parseLanguage(queryParameters.get("browser_locale"),EIsoLanguage.ENGLISH);
        final String url = request.getHeader(HttpHeaderConstants.REFERER);        
        final Map<String,String> urlParameters = HttpHeaderParser.parseQueryString(url);
        stationName = urlParameters.get("station");
        traceProfile = urlParameters.get("trace");
        language = parseLanguage(urlParameters.get("language"),browserLanguage);
        browserEngine = parseUserAgent(request.getHeader("user-agent"));
    }
    
    public boolean isSecurityOptionsAcceptable(final HttpServletRequest request){
        final X509Certificate[] certs = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
        return (!isSecure || request.isSecure()) && (certificates==null || certs!=null);
    }
    
    private static EIsoLanguage parseLanguage(final String language, final EIsoLanguage defaultLanguage){
        if (language != null && !language.isEmpty()) {
            try{
                if (language.length() > 2) {
                    return EIsoLanguage.getForValue(language.substring(0, 2).toLowerCase());
                }
                return EIsoLanguage.getForValue(language.toLowerCase());
            } catch (NoConstItemWithSuchValueError e) {
                return defaultLanguage;
            }
        }else{
            return defaultLanguage;
        }
    }
    
    private EWebBrowserEngineType parseUserAgent(final String userAgent) {
        if (userAgent!=null && !userAgent.isEmpty()){
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")){
                return EWebBrowserEngineType.MSIE;
            }else if (userAgent.contains("WebKit")){
                return EWebBrowserEngineType.WEBKIT;
            }else if (userAgent.contains("Gecko")){
                return EWebBrowserEngineType.GECKO;
            }
        }
        return EWebBrowserEngineType.OTHER;
    }        
    
    public boolean isSecure() {
        return isSecure;
    }

    public X509Certificate[] getClientCertificates() {
        return certificates;
    }

    public String getStationName() {
        return stationName;
    }
    
    public String getTraceProfile() {
        return traceProfile;
    }

    public EIsoLanguage getClientLanguage() {
        return language;
    }

    public EWebBrowserEngineType getBrowserEngineType(){
        return browserEngine;
    }
}