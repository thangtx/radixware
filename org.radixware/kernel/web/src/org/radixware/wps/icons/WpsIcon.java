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
package org.radixware.wps.icons;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.apache.batik.ext.awt.image.codec.png.PNGImageWriter;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.repository.fs.FSRepositoryBranch;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.wps.WebServer;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.icons.images.SvgImage;
import org.radixware.wps.icons.images.WsIcons;
import org.radixware.wps.rwt.RootPanel;
import org.radixware.wps.rwt.UIObject;

public class WpsIcon implements Icon {
    
    private int cacheKey;
    private String uri;
    private boolean isSvg;
    private URL resourceUrl;
    
    public WpsIcon(String uri, URL resourceUrl, boolean isSvg) {
        this.uri = "icons/" + uri;
        this.cacheKey = uri.hashCode();
        this.isSvg = isSvg;
        this.resourceUrl = resourceUrl;
    }
    
    public WpsIcon(String uri, boolean isSvg) {
        this.uri = uri;
        this.cacheKey = uri.hashCode();
        this.isSvg = isSvg;
    }
    
    public WpsIcon(WpsEnvironment env, File tempFile) {
        String extension = FileUtils.getFileExt(tempFile);
        String mimeType = "image/png";
        if ("svg".equals(extension)) {
            mimeType = "image/svg+xml";
        }
        
        String uuid = env.registerImageResource(tempFile.getAbsolutePath(), tempFile, mimeType);
        this.uri = uuid;
        this.cacheKey = uuid.hashCode();
        this.isSvg = true;
    }
    
    @Override
    public long cacheKey() {
        return cacheKey;
    }
    
    public String getURI(UIObject context) {
//        if (isSvg) {
//            return getScaledUri(w, h);
//        } else {
//            return uri;
//        }
        RootPanel root = null;
        if (context != null) {
            root = context.findRoot();
        }
        if (root == null) {// context is null or not in the tree
            WpsEnvironment env = (WpsEnvironment) context.getEnvironment();
            if (env != null) {
                root = env.getMainWindow();
            }
        }
        if (root != null) {
            return uri + "?root=" + root.getQueryId();
        } else {
            return uri;
        }
    }
    
    public Dimension getOriginalSize() {
        try {
            InputStream stream = getInputStream();
            
            if (isSvg) {
                try {
                    SvgImage image = SvgImage.Factory.newInstance(getInputStream());
                    return new Dimension(image.getWidth(), image.getHeight());
                } catch (IOException ex) {
                    return null;
                }
            } else {
                BufferedImage buf = ImageIO.read(stream);
                return new Dimension(buf.getWidth(), buf.getHeight());
            }
        } catch (IOException ex) {
            return null;
        }
    }
    
    public boolean isSvg() {
        return isSvg;
    }
    
    public static String getOriginalUrl(String fullUri, int[] dim) {
        int index = fullUri.indexOf(".svg_");
        if (index > 0 && fullUri.endsWith(".png")) {
            int start = index + 5;
            int end = fullUri.length() - 4;
            if (end <= start) {
                return fullUri;
            }
            String sizeString = fullUri.substring(start, end);
            index = sizeString.indexOf('_');
            if (index <= 0) {
                return fullUri;
            }
            try {
                int w = Integer.parseInt(sizeString.substring(0, index));
                int h = Integer.parseInt(sizeString.substring(index + 1));
                dim[0] = w;
                dim[1] = h;
                return fullUri.substring(0, start - 1);
            } catch (NumberFormatException e) {
                return fullUri;
            }
        } else {
            return fullUri;
        }
    }
    
    public void save(int[] size, HttpServletResponse rs) throws IOException {
//        if (size != null && size.length == 2 && size[0] > 0 && size[1] > 0) {
//            File file = getImageFile(size[0], size[1]);
//            if (file != null) {
//                FileInputStream input = null;
//                try {
//                    input = new FileInputStream(file);
//                    OutputStream out = rs.getOutputStream();
//                    rs.setContentType("image/png");
//                    try {
//                        FileUtils.copyStream(input, out);
//                        return;
//                    } finally {
//                        out.flush();
//                        out.close();
//                    }
//                } finally {
//                    if (input != null) {
//                        input.close();
//                    }
//                }
//            }
//        }
        InputStream input = getInputStream();
        if (input != null) {
            if (isSvg) {
                rs.setContentType("image/svg+xml");
            } else {
                rs.setContentType("image/png");
            }
            //keep one day
            rs.setHeader("Cache-Control", "max-age=86400, public, must-revalidate");
            OutputStream out = rs.getOutputStream();
            
            try {
                FileUtils.copyStream(input, out);
            } finally {
                out.flush();
                out.close();
                input.close();
            }
        }
        
    }
    
    public String getImageFileName(int w, int h) {
        int index = uri.lastIndexOf('/');
        if (index < 0) {
            return uri;
        } else {
            return uri.substring(index + 1) + "_" + w + "_" + h + ".png";
        }
    }
    
    public String getScaledUri(int w, int h) {
        return uri;// + "_" + w + "_" + h + ".png";
    }
    
    public File getImageFile(int w, int h) throws IOException {
        return getImageFile(w, h, false);
    }
    
    public File getImageFile(int w, int h, boolean force) throws IOException {
        String fileName = getImageFileName(w, h);
        File file = new File(WebServer.getImagesDir(), fileName);
        FileLock lock = lock(file);
        if (lock == null) {
            return null;
        }
        try {
            
            if (file.exists() && !force) {                
                return file;
            } else {
                try {
                    SvgImage image = SvgImage.Factory.newInstance(getInputStream(), w, h);
                    PNGImageWriter writer = new PNGImageWriter();
                    FileOutputStream stream = null;
                    try {
                        stream = new FileOutputStream(file);
                        writer.writeImage(image, stream);
                    } finally {
                        if (stream != null) {
                            stream.close();
                        }
                    }
                    return file;
                } catch (Throwable ex) {
                    if (file.exists()) {
                        FileUtils.deleteFile(file);
                    }
                    return null;
                }
            }
        } finally {
            unlock(lock);
        }
    }
    
    private InputStream getInputStream() throws IOException {
        if (resourceUrl != null) {
            return resourceUrl.openStream();
        } else {
            return WsIcons.getIconInputStream(uri);
        }
    }
    
    private static FileLock lock(File file) throws IOException {
        FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
        final long t = System.currentTimeMillis();
        for (int i = 0;; i++) {
            try {
                FileLock l = fc.tryLock(0L, Long.MAX_VALUE, false);
                if (l != null) {
                    if (i > 10) {
                        Logger.getLogger(FSRepositoryBranch.class.getName()).log(Level.FINE, "Lock done");
                    }
                    return l;
                }
                final long delay = System.currentTimeMillis() - t;
                if (delay > 3000) {
                    return null;
                }
                if (i == 10) {
                    Logger.getLogger(FSRepositoryBranch.class.getName()).log(Level.FINE, "Try to lock {0}... ", file.getName());
                }
                
            } catch (IOException | OverlappingFileLockException ex) {
                //try again later
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
            
        }
    }
    
    private void unlock(FileLock lockHandle) throws IOException {
        if (lockHandle != null) {
            lockHandle.release();
            lockHandle.channel().close();
        }
    }
}
