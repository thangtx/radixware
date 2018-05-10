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
import java.util.HashMap;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.ssl.CertificateUtils;


public final class HttpConnectionOptions {

    private final boolean isSecure;
    private final boolean isAdminPanel;
    private final String userNameCertAttr;
    private final X509Certificate[] certificates;
    private final EIsoLanguage browserLanguage;
    private final EIsoLanguage urlLanguage;    
    private final EWebBrowserEngineType browserEngine;
    private final Map<String,String> urlParameters;
    private final Map<String, String> customParamsMap = new HashMap<>();
    
    HttpConnectionOptions(final HttpServletRequest request, final String userNameCertAttr, final String urlParams){
        isSecure = request.isSecure();
        this.userNameCertAttr = userNameCertAttr;
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
        browserLanguage = 
                parseLanguage(queryParameters.get("browser_locale"),null);
        final String url = request.getHeader(HttpHeaderConstants.REFERER);        
        urlParameters = HttpHeaderParser.parseQueryString(urlParams==null ? url : urlParams);
        urlLanguage = parseLanguage(urlParameters.get("language"),null);
        final String adminUrlParam = WebServerRunParams.getAdminPanelUrlParam();
        if (isSecure 
            && adminUrlParam!=null 
            && !adminUrlParam.isEmpty() 
            && urlParameters.containsKey(adminUrlParam)){
            final String userName = getUserNameFromCertificate();
            isAdminPanel = userName!=null && !userName.isEmpty() && WebServerRunParams.getAdminUserNames().contains(userName);
        }else{
            isAdminPanel = false;
        }
        for (Entry<String, String> entry : urlParameters.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("_")) {
                customParamsMap.put(key.substring(1), entry.getValue());
            }
        }
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
    
    public String getUserNameFromCertificate() {
        if (certificates!=null && certificates.length>0){            
            final Map<String, String> dn
                    = CertificateUtils.parseDistinguishedName(certificates[0].getSubjectDN().getName());
            return dn.get(userNameCertAttr);
        }else{
            return null;
        }
    }    

    public String getStationName() {
        return urlParameters.get("station");
    }
    
    public String getExplorerRootId() {
        return urlParameters.get("explorerRoot");
    }
    
    public String getUserName() {
        return urlParameters.get("user");
    }
    
    public String getTraceProfile() {
        return urlParameters.get("trace");
    }
    
    public String getPassword() {
        return urlParameters.get("password");
    }
    
    public String getPwdHash256() {
        return urlParameters.get("pwdHash256");
    }
    
    public String getPwdHash1() {
        return urlParameters.get("pwdHash1");
    }
    
    public EIsoLanguage getBrowserLanguage() {
        return browserLanguage;
    }
    
    public EIsoLanguage getUrlLanguage() {
        return urlLanguage;
    }

    public EWebBrowserEngineType getBrowserEngineType(){
        return browserEngine;
    }
    
    public boolean isAdminPanel(){
        return isAdminPanel;
    }
    
    public String getEntryPoint(){
        return urlParameters.get("entryPoint");
    }
    
    public Map<String,String> getCustomParams(){
        final Map<String,String> params = new HashMap<>(customParamsMap);
        return params;
    }
}