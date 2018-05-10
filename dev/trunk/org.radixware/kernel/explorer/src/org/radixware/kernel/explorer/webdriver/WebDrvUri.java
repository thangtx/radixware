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

package org.radixware.kernel.explorer.webdriver;

import java.util.EnumMap;
import org.radixware.kernel.explorer.webdriver.elements.UIElementId;
import java.util.UUID;
import org.radixware.kernel.explorer.webdriver.commands.ElementScreenshotCmd;
import org.radixware.kernel.explorer.webdriver.commands.GetAvailableLogTypesCmd;
import org.radixware.kernel.explorer.webdriver.commands.GetLogCmd;
import org.radixware.kernel.explorer.webdriver.commands.SendKeysToActiveElementCmd;
import org.radixware.kernel.explorer.webdriver.commands.UploadFileCmd;

public class WebDrvUri {
    
    public static enum EUriPart{SESSION_ID, COMMAND_NAME, COMMAND_GROUP_NAME,
                                ELEMENT_ID, PROPERTY_NAME, ATTRIBUTE_NAME,
                                CSS_PROPERTY_NAME, COOKIE_NAME};
                                
    private final EnumMap<EUriPart,String> parts = new EnumMap<>(EUriPart.class);

    private WebDrvUri(){};
        
    public static WebDrvUri parse(final String uriPath){
        final WebDrvUri uri = new WebDrvUri();
        final String[] parts = uriPath.split("/");
        if (parts.length==1){
            uri.parts.put(EUriPart.COMMAND_NAME, parts[0]);
        }else if ("session".equals(parts[0])){
            uri.parts.put(EUriPart.SESSION_ID, parts[1]);

            // session/<sessionId>/keys
			if(parts.length == 3 && parts[2].equals("keys"))
			{
				uri.parts.put(EUriPart.COMMAND_NAME, SendKeysToActiveElementCmd.NAME);
				return uri;
			}
			
            // session/<sessionId>/file
			if(parts.length == 3 && parts[2].equals("file")) {
				uri.parts.put(EUriPart.COMMAND_NAME, UploadFileCmd.NAME);
				return uri;
			}
			
            // session/<sessionId>/element/<elementId>/screenshot
			if(parts.length == 5 && parts[2].equals("element")
					&& parts[4].equals("screenshot"))
			{
				uri.parts.put(EUriPart.COMMAND_NAME, ElementScreenshotCmd.NAME);
				uri.parts.put(EUriPart.ELEMENT_ID, parts[3]);
				return uri;
			}
			
            // session/<sessionId>/log/types
			if(parts.length == 4 && parts[2].equals("log")
					&& parts[3].equals("types")) {
				uri.parts.put(EUriPart.COMMAND_NAME, GetAvailableLogTypesCmd.NAME);
				return uri;
			}
			
            // session/<sessionId>/log
			if(parts.length == 3 && parts[2].equals("log"))
			{
				uri.parts.put(EUriPart.COMMAND_NAME, GetLogCmd.NAME);
				return uri;
			}
			
			else {
				if (parts.length==2){
					uri.parts.put(EUriPart.COMMAND_NAME, "session");
				}else{
					if (parts.length==3){
						uri.parts.put(EUriPart.COMMAND_NAME, parts[2]);
					}else if (parts.length==4){
						uri.parts.put(EUriPart.COMMAND_GROUP_NAME, parts[2]);
						if ("cookie".equals(parts[2])){
							uri.parts.put(EUriPart.COMMAND_NAME, parts[2]);
							uri.parts.put(EUriPart.COOKIE_NAME, parts[3]);
						}else{
							uri.parts.put(EUriPart.COMMAND_NAME, parts[3]);
						}
					}else if ("element".equals(parts[2])){
						if ("active".equals(parts[3])){
							uri.parts.put(EUriPart.COMMAND_GROUP_NAME, parts[2]);
							uri.parts.put(EUriPart.COMMAND_NAME, parts[3]);
						}else{
							uri.parts.put(EUriPart.ELEMENT_ID, parts[3]);
							if (parts.length==6){
								if ("property".equals(parts[4])){
									uri.parts.put(EUriPart.COMMAND_NAME, parts[4]);
									uri.parts.put(EUriPart.PROPERTY_NAME, parts[5]);
								}else if ("attribute".equals(parts[4])){
									uri.parts.put(EUriPart.COMMAND_NAME, parts[4]);
									uri.parts.put(EUriPart.ATTRIBUTE_NAME, parts[5]);
								}else if ("css".equals(parts[4])){
									uri.parts.put(EUriPart.COMMAND_NAME, parts[4]);
									uri.parts.put(EUriPart.CSS_PROPERTY_NAME, parts[5]);
								}else{
									uri.parts.put(EUriPart.COMMAND_NAME, parts[5]);
								}
							}else{
								uri.parts.put(EUriPart.COMMAND_NAME, parts[4]);
							}
						}
					}else{
						throw new IllegalArgumentException("Failed to parse URI path \'"+uriPath+"\'"); 
					}
				}
			}
        }
        return uri;
    }

    public UUID getSessionId() {
        final String sessionId = parts.get(EUriPart.SESSION_ID);
        return sessionId==null || sessionId.isEmpty() ? null : UUID.fromString(sessionId);
    }

    public String getCommandName() {
        return parts.get(EUriPart.COMMAND_NAME);
    }

    public String getCommandGroupName() {
        return parts.get(EUriPart.COMMAND_GROUP_NAME);
    }

    public UIElementId getElementId() {
        final String elementId = parts.get(EUriPart.ELEMENT_ID);
        return elementId==null || elementId.isEmpty() ? null : UIElementId.fromString(elementId);
    }

    public String getPart(final EUriPart part){
        return parts.get(part);
    }

    @Override
    public String toString() {
        final StringBuilder strBuilder = new StringBuilder("WebDrvUri{");
        for (EUriPart part: EUriPart.values()){
            if (parts.containsKey(part)){
                if (strBuilder.length()>1){
                    strBuilder.append(", ");                    
                }
                strBuilder.append(part.name());
                strBuilder.append('=');
                strBuilder.append(parts.get(part));
            }
        }
        strBuilder.append('}');
        return strBuilder.toString();
    }
}
