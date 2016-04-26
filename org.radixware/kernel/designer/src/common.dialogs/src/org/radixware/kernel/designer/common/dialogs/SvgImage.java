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

package org.radixware.kernel.designer.common.dialogs;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.ext.awt.RenderingHintsKeyExt;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGLength;
import org.w3c.dom.svg.SVGSVGElement;

/**
 * SvgImage - cover under BufferedImage, loads SVG Document, paints it on
 * BufferedImage, in the sequel looks like BufferedImage, without vector scaling
 * and other vector features. Only
 * {@linkplain SvgImage#getScaledInstance(int, int, int)} (inherited from Image)
 * supports true vector scaling.
 *
 */
/*
 * It is impossigle to display true SVG image in Netbeans because Invalid Image Variant exception raised :-(
 */
class SvgImage extends BufferedImage {

    private final GraphicsNode rootSvgNode;
    private final double originalWidth;
    private final double originalHeight;

    protected SvgImage(GraphicsNode rootSvgNode, int requiredWidth, int requiredHeight, double originalWidth, double originalHeight) {
        super(requiredWidth, requiredHeight, BufferedImage.TYPE_INT_ARGB);
        this.rootSvgNode = rootSvgNode;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        paintGraphics();
    }

    private void paintGraphics() {
        Graphics2D graphics = (Graphics2D) getGraphics();

        // Use best quality
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        // Scale image to desired size and maintain aspect ratio.
        AffineTransform transformer = new AffineTransform();
//        Rectangle2D rect = rootSvgNode.getRoot().getBounds();
//        double hScale = ((double) getWidth()) / (rect.getWidth() + rect.getX());
//        double vScale = ((double) getHeight()) / (rect.getHeight() + rect.getY());
        double hScale = ((double) getWidth()) / originalWidth;
        double vScale = ((double) getHeight()) / originalHeight;
        double scale = Math.min(vScale, hScale);
        transformer.scale(scale, scale);
        graphics.transform(transformer);

        rootSvgNode.setRenderingHint(RenderingHintsKeyExt.KEY_BUFFERED_IMAGE, new WeakReference<SvgImage>(this));

        rootSvgNode.paint(graphics);
    }

    @Override
    public Image getScaledInstance(int width, int height, int hints) {
        return new SvgImage(this.rootSvgNode, width, height, originalWidth, originalHeight);
    }

    public static final class Factory {

        private Factory() {
        }

        /**
         * Load SVG Document from input stream.
         *
         * @throws java.io.IOException
         */
        private static SVGDocument loadSvgDocument(InputStream x) throws IOException {
            // create SVG Document
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
            SVGDocument svgDocument = (SVGDocument) factory.createDocument(null, x);
            return svgDocument;
        }

        /**
         * Get root GraphicsNode of the specified SVGDocument.
         */
        private static GraphicsNode getRootGraphicsNode(SVGDocument svgDocument) throws IOException {
            final UserAgentAdapter userAgentAdapter = new UserAgentAdapter();
            final BridgeContext bridgeContext = new BridgeContext(userAgentAdapter);
            final GVTBuilder builder = new GVTBuilder();
            final GraphicsNode graphicsNode = builder.build(bridgeContext, svgDocument);
            return graphicsNode;
        }

        private static double getWidthInPx(SVGAnimatedLength animatedLength, double defaults) {
            final SVGLength length = animatedLength.getBaseVal();
            final int initType = length.getUnitType();
            final double width = length.getValueInSpecifiedUnits();
            switch (initType) {
                case SVGLength.SVG_LENGTHTYPE_PX:
                case SVGLength.SVG_LENGTHTYPE_NUMBER:
                    return width;
                case SVGLength.SVG_LENGTHTYPE_PT:
                    return width * 1.25;
                default:
                    return defaults;                
            }
        }

        public static SvgImage newInstance(InputStream x, boolean scale, int width, int height) throws IOException {
            final SVGDocument svgDocument = loadSvgDocument(x);
            final SVGSVGElement rootElement = svgDocument.getRootElement();

            final GraphicsNode rootGraphicsNode = getRootGraphicsNode(svgDocument);

            Rectangle2D bounds = rootGraphicsNode.getBounds();
            final double originalWidth = getWidthInPx(rootElement.getWidth(), bounds == null?0:bounds.getWidth());
            final double originalHeight = getWidthInPx(rootElement.getHeight(), bounds == null?0:bounds.getHeight());
            int requiredWidth = (scale ? width : (int) originalWidth);
            int requiredHeight = (scale ? height : (int) originalHeight);
            final SvgImage svgImage = new SvgImage(rootGraphicsNode, requiredWidth, requiredHeight, originalWidth, originalHeight);
            return svgImage;
        }

        /**
         * Load SVGImage from the specified SVGDocument and scale it to
         * specified size.
         *
         * @throws java.io.IOException
         */
        public static SvgImage newInstance(InputStream x, int width, int height) throws IOException {
            boolean scale = true;
            return newInstance(x, scale, width, height);
        }

        /**
         * Load SVGImage from the specified SVGDocument.
         *
         * @throws java.io.IOException
         */
        public static SvgImage newInstance(InputStream x) throws IOException {
            boolean scale = false;
            return newInstance(x, scale, 0, 0);
        }
    }
}
