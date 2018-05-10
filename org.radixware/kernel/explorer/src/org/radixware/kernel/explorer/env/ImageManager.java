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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.core.QBuffer;
import com.trolltech.qt.core.QIODevice;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QIconEngineV2;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QImageIOHandler;
import com.trolltech.qt.gui.QImageReader;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QStyleOption;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.types.RdxIcon;
import org.radixware.kernel.common.utils.SystemTools;



public class ImageManager implements org.radixware.kernel.common.client.env.ImageManager {    
    
    private static final class ScalableIconEngine extends QIconEngineV2 implements Cloneable{
        
        private final static List<ScalableIconEngine> INSTANCES = new LinkedList<>();
        
        private final byte[] content;
        private final QColor background;
        private final Map<String,QPixmap> pixmapsByMode = new HashMap<>(4);
        private final boolean isKernelIcon;
                
        @SuppressWarnings("LeakingThisInConstructor")
        public ScalableIconEngine(final byte[] content, final QColor background, final boolean kernelIcon){
            this.content = content;
            this.background = background;
            this.isKernelIcon = kernelIcon;
            INSTANCES.add(this);
        }

        @Override
        public void paint(final QPainter painter, final QRect rect, final QIcon.Mode mode, final QIcon.State state) {
            painter.drawPixmap(rect,pixmap(rect.size(), mode, state));            
        }

        @Override
        public QSize actualSize(QSize size, QIcon.Mode mode, QIcon.State state) {
            return size;
        }

        @Override
        public String key() {
            return ScalableIconEngine.class.getName();
        }        

        @Override
        public QIconEngineV2 clone() {
            return new ScalableIconEngine(content, background, isKernelIcon);
        }

        @Override
        public QPixmap pixmap(final QSize size, final QIcon.Mode mode, final QIcon.State state) {
            final String cacheKey = String.valueOf(size.height())+"_"+String.valueOf(size.width())+"_"+mode.name();
            QPixmap pixmap = pixmapsByMode.get(cacheKey);
            if (pixmap==null){
                if (mode==QIcon.Mode.Normal){
                    final QBuffer buffer = new QBuffer();
                    buffer.setData(content);
                    buffer.open(QIODevice.OpenModeFlag.ReadOnly);                
                    final QImageReader reader = new QImageReader(buffer);                
                    try{
                        reader.setQuality(100);
                        final QSize scaledSize;
                        if (SystemTools.isOSX){
                            final double pixelRatio = QApplication.instance().devicePixelRatio();                            
                            final int scaledWidth = (int)Math.round(size.width()*pixelRatio);
                            final int scaledHeight = (int)Math.round(size.height() * pixelRatio);
                            scaledSize = new QSize(scaledWidth, scaledHeight);
                        }else{
                            scaledSize = size;
                        }
                        reader.setScaledSize(scaledSize);
                        reader.setBackgroundColor(background);                        
                        pixmap = QPixmap.fromImageReader(reader);
                        if (SystemTools.isOSX){
                            pixmap.setDevicePixelRatio(QApplication.instance().devicePixelRatio());
                        }
                    }
                    finally{
                        buffer.close();
                        reader.dispose();
                    }
                }else{
                    final QPixmap normalPixmap = pixmap(size, QIcon.Mode.Normal, state);
                    final QStyleOption styleOption = new QStyleOption();
                    styleOption.setPalette(QApplication.palette());
                    pixmap = QApplication.style().generatedIconPixmap(mode, normalPixmap, styleOption);
                }
                pixmapsByMode.put(cacheKey, pixmap);
            }
            return pixmap;
        }
    
        void clear(){
            for (QPixmap pixmap: pixmapsByMode.values()){
                pixmap.dispose();
            }
            pixmapsByMode.clear();
            dispose();
        }
        
        static void clearAll(){
            for (ScalableIconEngine engine: INSTANCES){
                engine.clear();
            }
            INSTANCES.clear();
        }        
    }    
    
    private static final class ScalableIcon extends RdxIcon{
        
        private final ScalableIconEngine engine;        
        
        public ScalableIcon(final ScalableIconEngine engine, final String cacheKey){
            super(engine,cacheKey);
            this.engine = engine;
        }
        
        public QIcon downcast(){
           return new QIcon(engine.clone());
        }
    }

    private final static String CLASSPATH_PROTOCOL = "classpath:";
    private final static String EXPLORER_DIR = "org/radixware/kernel/common/client/";
    
    private final static Map<String, RdxIcon> KERNEL_ICONS_CACHE = new HashMap<>(128);        
    private final static Map<String, RdxIcon> ADS_ICONS_CACHE = new HashMap<>(128);
    
    private final static Map<String, QIcon> QICONS_CACHE = new HashMap<>(128);
    
    private final Application env;
    private final QBuffer buffer;
    private final QImageReader imageReader;

    public ImageManager(Application env) {
        this.env = env;
        buffer = new QBuffer(env);
        imageReader = new QImageReader();
    }

    @Override
    public Icon loadIcon(String path) {
        return loadQIcon(path);
    }
    
    private static boolean isKernelImage(final String fileName){
        return fileName.startsWith(CLASSPATH_PROTOCOL);
    }
    
    private static RdxIcon getCachedRdxIcon(final boolean isKernelIcon, final String fileName){
        return isKernelIcon ? KERNEL_ICONS_CACHE.get(fileName) : ADS_ICONS_CACHE.get(fileName);
    }
    
    private static void cacheRdxIcon(final boolean isKernelIcon, final String fileName, RdxIcon icon){
        if (isKernelIcon){
            KERNEL_ICONS_CACHE.put(fileName, icon);
        }else{
            ADS_ICONS_CACHE.put(fileName, icon);
        }
    }
    
    private boolean isIconCached(final boolean isKernelIcon, final String fileName){
        return isKernelIcon ? KERNEL_ICONS_CACHE.containsKey(fileName) : ADS_ICONS_CACHE.containsKey(fileName);
    }

    private URL getImageUrl(final String fileName) {
        final String actualFileName;
        if (fileName.startsWith(CLASSPATH_PROTOCOL + "images/")
                || fileName.startsWith(CLASSPATH_PROTOCOL + "/images/")
                || fileName.startsWith(CLASSPATH_PROTOCOL + "//images/")) {
            final int idx = fileName.indexOf("images/");
            final String path = fileName.substring(idx);
            actualFileName = EXPLORER_DIR + path;
        } else if (fileName.startsWith(CLASSPATH_PROTOCOL)) {
            actualFileName = fileName.substring(CLASSPATH_PROTOCOL.length());
        } else {
            actualFileName = fileName;
        }

        final ClassLoader loader;
        if (env.isReleaseRepositoryAccessible()) {
            loader = env.getDefManager().getClassLoader();
        } else {
            loader = Thread.currentThread().getContextClassLoader();
        }
        final URL imageUrl = loader.getResource(actualFileName);
        if (imageUrl == null) {
            throw new DefinitionError("File not found \"" + actualFileName + "\"");
        }
        return imageUrl;

    }

    private byte[] readImage(final URL url) {
        final InputStream stream;
        try {
            stream = url.openStream();
        } catch (IOException ex) {
            final String message = env.getMessageProvider().translate("ExplorerError", "Can't read file \'%s\':\n%s\n%s");
            env.getTracer().error(String.format(message, url.toExternalForm(), ex.getMessage(),
                    ClientException.exceptionStackToString(ex)));
            return null;
        }
        try {
            final byte[] content = new byte[stream.available()];
            int n = 0;
            while (n < content.length) {
                int count = stream.read(content, n, content.length - n);
                if (count < 0) {
                    final String message = env.getMessageProvider().translate("ExplorerError", "Can't read file \'%s\'");
                    env.getTracer().error(String.format(message, url.toExternalForm()));
                    return null;
                }
                n += count;
            }
            return content;
        } catch (IOException ex) {
            final String message = env.getMessageProvider().translate("ExplorerError", "Can't read file \'%s\':\n%s\n%s");
            env.getTracer().error(String.format(message, url.toExternalForm(), ex.getMessage(), ClientException.exceptionStackToString(ex)));
            return null;
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {//NOPMD
            }
        }
    }

    RdxIcon loadQIcon(final String fileName) {
        final URL url = getImageUrl(fileName);
        final boolean isKernelImage = isKernelImage(fileName);
        final String cacheKey = url.toExternalForm();        
        if (isIconCached(isKernelImage, cacheKey)){
            return getCachedRdxIcon(isKernelImage, cacheKey);
        }
        final byte[] content = readImage(url);
        if (content == null) {
            return null;
        }
        final QBuffer iconBuffer = Application.isInMainThread() ? buffer : new QBuffer();
        iconBuffer.setData(content);
        iconBuffer.open(QIODevice.OpenModeFlag.ReadOnly);
        imageReader.setDevice(iconBuffer);
        try {
            final RdxIcon result;
            if (imageReader.supportsOption(QImageIOHandler.ImageOption.ScaledSize)){
                final ScalableIconEngine engine = new ScalableIconEngine(content,QColor.transparent,isKernelImage);
                result = new ScalableIcon(engine, cacheKey);
            }else{
                imageReader.setQuality(100);
                final QImage img = imageReader.read();
                if (img.isNull()) {
                    return null;
                }else{
                    final QPixmap pixmap = new QPixmap();
                    pixmap.loadFromData(content);
                    result = new RdxIcon(pixmap, cacheKey);
                }
            }
            cacheRdxIcon(isKernelImage, cacheKey, result);
            return result;
        } finally {
            iconBuffer.close();
            if (!Application.isInMainThread()){
                iconBuffer.dispose();
            }
        }
    }

    public RdxIcon loadSvgIcon(final String fileName, final QColor background) {
        final URL url = getImageUrl(fileName);
        final boolean isKernelImage = isKernelImage(fileName);
        final String key = url.toExternalForm() + "\n" + background.name();
        if (isIconCached(isKernelImage, key)){
            return getCachedRdxIcon(isKernelImage, key);
        }
        final byte[] content = readImage(url);
        if (content == null) {
            return null;
        }
        final ScalableIconEngine engine = new ScalableIconEngine(content, background,isKernelImage);
        final RdxIcon result = new ScalableIcon(engine, key);
        cacheRdxIcon(isKernelImage, key, result);
        return result;
    }

    @Override
    public Id findCachedIconId(final Icon icon) {
        return findCachedIconIdByCacheKey(icon.cacheKey());
    }

    public static Id findCachedIconIdByCacheKey(final long cacheKey) {
        for (Map.Entry<String, RdxIcon> entries : ADS_ICONS_CACHE.entrySet()) {
            if (entries.getValue().cacheKey() == cacheKey) {
                final String key = entries.getKey();
                final String url = key.contains("\n") ? key.substring(0, key.indexOf('\n')) : key;
                if (url.lastIndexOf('/') <= 0) {
                    return null;
                }
                final String fileName = url.substring(url.lastIndexOf('/') + 1);
                final String idAsStr = fileName.indexOf('.') > 0 ? fileName.substring(0, fileName.indexOf('.')) : fileName;
                return Id.Factory.loadFrom(idAsStr);
            }
        }
        return null;
    }

    @Override
    public void clearCache(final boolean clearAdsIconsOnly) {
        ADS_ICONS_CACHE.clear();
        if (!clearAdsIconsOnly){
            KERNEL_ICONS_CACHE.clear();
            QICONS_CACHE.clear();
            ScalableIconEngine.clearAll();
        }
    }

    public static QIcon getQIcon(final Icon icon){
        if (icon instanceof RdxIcon){
            //RADIX-9299 It must be explicitly QIcon instance to return it as data in QAbstractItemModel
            final String cacheKey = ((RdxIcon)icon).getCacheKey();
            QIcon result = QICONS_CACHE.get(cacheKey);
            if (result==null){
                if (icon instanceof ScalableIcon){
                    result = ((ScalableIcon)icon).downcast();
                }else{
                    result = new QIcon((RdxIcon)icon);
                }
                QICONS_CACHE.put(cacheKey, result);
            }
            return result;
        }
        return icon instanceof QIcon ? (QIcon)icon : null;
    }

    @Override
    public Icon getIcon(final ClientIcon icon) {
        return loadIcon(icon.fileName);
    }
}