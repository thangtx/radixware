/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.utils;

import com.trolltech.qt.core.QSettings;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleFactory;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.env.Application;


public enum EQtStyle {
    
    Windows("Windows"),
    WindowsXp("WindowsXP"),
    WindowsVista("WindowsVista"),
    Motif("Motif"),
    Cde("CDE"),
    Plastique("Plastique"),
    Cleanlooks("Cleanlooks"),
    Gtk("GTK"),
    MacintoshAqua("Macintosh (aqua)"),
    Unknown(null);
    
    private static EQtStyle DEFAULT = null;    
    private static EnumSet<EQtStyle> SUPPORTED_STYLES = null;
    private static List<String> SUPPORTED = null;
            
    private final String title;
    private final boolean isSupported;
    
    private EQtStyle(final String title){
        this.title = title;
        isSupported = isSupported(title);
    }
    
    public String getTitle(){
        return title;
    }
    
    public boolean isSupported(){
        return isSupported;
    }    
    
    public final static EQtStyle getFromTitle(final String title){
        if (title==null || title.isEmpty()){
            return Unknown;
        }
        for (EQtStyle style: EQtStyle.values()){
            if (title.equalsIgnoreCase(style.title)){
                return style;
            }
        }
        if ("gtk+".equalsIgnoreCase(title)){
            return Gtk;
        }
        if ("macintosh".equalsIgnoreCase(title)){
            return MacintoshAqua;
        }
        return Unknown;
    }
    
    public final static EQtStyle detectStyle(final QStyle style){
        final EQtStyle result = style==null ? Unknown : getFromTitle(style.objectName());
        if (result==Unknown){
            return getFromTitle(Application.getCurrentStyleName());
        }else{
            return result;
        }
    }  
    
    public static EQtStyle getDefault(){
        if (DEFAULT==null){
            DEFAULT = detectDefault();
        }
        return DEFAULT;
    }
    
    public static EnumSet<EQtStyle> getSupported(){
        if (SUPPORTED_STYLES==null){
            SUPPORTED_STYLES = EnumSet.noneOf(EQtStyle.class);
            for (EQtStyle style: values()){
                if (style.isSupported){
                    SUPPORTED_STYLES.add(style);
                }
            }
        }
        return SUPPORTED_STYLES;
    }
    
    private static boolean isSupported(final String styleName){
        if (styleName==null || styleName.isEmpty()){
            return false;
        }
        if (SUPPORTED==null){
            final List<String> supportedStyles = QStyleFactory.keys();
            SUPPORTED = new LinkedList<>();
            for (String style: supportedStyles){
                if (style!=null && !style.isEmpty()){
                    SUPPORTED.add(style.toLowerCase());
                }
            }
        }
        if (SUPPORTED.contains(styleName.toLowerCase())){
            return true;
        }
        if ("gtk".equalsIgnoreCase(styleName)){
            return isSupported("gtk+");
        }
        if ("macintosh".equalsIgnoreCase(styleName)){
            return isSupported(styleName+" (aqua)");
        }
        return false;
    }
    
    private static EQtStyle detectDefault(){
        final String paramStyle = RunParams.getQtStyle();
        if (isSupported(paramStyle)){
            return getFromTitle(paramStyle);
        }
        final QSettings qtSettings = new QSettings(QSettings.Scope.UserScope, "Trolltech");
        qtSettings.beginGroup("Qt");
        final String settingsStyle = (String)qtSettings.value("style", "");
        if (isSupported(settingsStyle)){
            return getFromTitle(settingsStyle);
        }
        final String envStyle = System.getenv("QT_STYLE_OVERRIDE");
        if (isSupported(envStyle)){
            return getFromTitle(envStyle);
        }
        final EQtStyle desktopStyle = detectDesktopEnvironmentStyle();
        if (desktopStyle.isSupported()){
            return desktopStyle;
        }
        for (EQtStyle style: values()){
            if (style.isSupported){
                return style;
            }
        }
        return Unknown;
    }
    
    private static EQtStyle detectDesktopEnvironmentStyle(){
        if (SystemTools.isWindows){
            final String version = System.getProperty("os.version");
            if (version==null || version.isEmpty()){
                return Windows;
            }
            if (version.startsWith("5.1")){
                return WindowsXp;
            }            
            final Pattern p = Pattern.compile("(\\d).*");
            final Matcher m = p.matcher(version);
            int majorVersion;
            if (m.matches()){
                try{
                    majorVersion = Integer.parseInt(m.group(1));
                }catch(IllegalStateException | IndexOutOfBoundsException | NumberFormatException e){
                    majorVersion = -1;
                }
            }else{
                majorVersion = -1;
            }
            return majorVersion>=6 ? WindowsVista : Windows;
        }else if (SystemTools.isSunOS){
            return Cde;
        }else if (SystemTools.isOSX){
            return MacintoshAqua;
        }else{//some *nix like
            if (System.getenv("KDE_FULL_SESSION")!=null){
                return Plastique;
            }else if ("gnome".equals(System.getenv("DESKTOP_SESSION")) || System.getenv("GNOME_DESKTOP_SESSION_ID")!=null){
                return Gtk.isSupported ? Gtk : Cleanlooks;
            }
            return Windows;
        }                        
    }
}
