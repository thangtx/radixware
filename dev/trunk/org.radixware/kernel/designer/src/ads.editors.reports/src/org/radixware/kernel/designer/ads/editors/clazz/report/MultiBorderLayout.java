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

package org.radixware.kernel.designer.ads.editors.clazz.report;
import static java.util.Arrays.asList;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;


public class MultiBorderLayout  extends BorderLayout{
  
 private interface DimensionGetter{
    Dimension get(final Component in);
 }
  
 private static final DimensionGetter getMinimumSize=new DimensionGetter(){
    @Override
    public Dimension get(final Component component){
        return component.getMinimumSize();
    }
 };
  
 private static final DimensionGetter getPreferredSize=new DimensionGetter(){
    @Override
    public Dimension get(final Component component){
        return component.getPreferredSize();
    }
 };
  
 private static final DimensionGetter getMaximumSize=new DimensionGetter(){
    @Override
    public Dimension get(final Component component){
        return component.getMaximumSize();
    }
 };

 private final List<Component> northComponents=new ArrayList<>();
 private final List<Component> southComponents=new ArrayList<>();
 private final List<Component> westComponents=new ArrayList<>();
 private final List<Component> eastComponents=new ArrayList<>();
 private final List<Component> centerComponents=new ArrayList<>();
    
 public MultiBorderLayout(){
    super(0,0);
 }
 
 @Override
 public void addLayoutComponent(final Component comp,final Object name){
    synchronized(comp.getTreeLock()){
        if(name != null && name instanceof String){
            switch ((String)name) {
                case CENTER:
                    centerComponents.add(comp);
                    return;
                case NORTH:
                    northComponents.add(comp);
                    return;
                case SOUTH:
                    southComponents.add(comp);
                    return;
                case WEST:
                    westComponents.add(comp);
                    return;
                case EAST:
                    eastComponents.add(comp);
                    return;                  
            }
        }
        throw new IllegalArgumentException( "unknown BorderLayout constraint: " + name);
    }
 }

/* @Override
 public void addLayoutComponent(final String name,final Component comp){
    synchronized(comp.getTreeLock()){
        if(name != null){
            switch (name) {
                case CENTER:
                    centerComponents.add(comp);
                    return;
                case NORTH:
                    northComponents.add(comp);
                    return;
                case SOUTH:
                    southComponents.add(comp);
                    return;
                case WEST:
                    westComponents.add(comp);
                    return;
                case EAST:
                    eastComponents.add(comp);
                    return;                  
            }
        }
        throw new IllegalArgumentException( "unknown BorderLayout constraint: " + name);
    }
 }*/
  
 @Override
 public void removeLayoutComponent(final Component component){
    synchronized(component.getTreeLock()){
        centerComponents.remove(component);
        westComponents.remove(component);
        eastComponents.remove(component);  
        northComponents.remove(component);
        southComponents.remove(component);              
    }
 }
  
 @Override
 public Dimension minimumLayoutSize(final Container target){
    return layoutSize(target,getMinimumSize);
 }
  
 @Override
 public Dimension preferredLayoutSize(final Container target){
    return layoutSize(target,getPreferredSize);
 }
  
 @Override
 public Dimension maximumLayoutSize(Container target){
    return layoutSize(target,getMaximumSize);
 }
  
 private Dimension layoutSize(final Container target, final DimensionGetter dimensionGetter){
    synchronized(target.getTreeLock()){
        final Dimension northSize=sumHorizontal(northComponents,dimensionGetter);
        final Dimension southSize=sumHorizontal(southComponents,dimensionGetter);
        final Dimension westSize=sumVertical(westComponents,dimensionGetter);
        final Dimension eastSize=sumVertical(eastComponents,dimensionGetter);
        final Dimension centerSize=sumCenter(centerComponents,dimensionGetter);

        final Dimension dimension=new Dimension(max(northSize.width, southSize.width, westSize.width + centerSize.width + eastSize.width), 
                northSize.height + max(westSize.height,centerSize.height,eastSize.height) + southSize.height);
        final Insets insets=target.getInsets();
        dimension.width+=insets.left + insets.right;
        dimension.height+=insets.top + insets.bottom;
        return dimension;
    }
 }
  
 @Override
 public void layoutContainer(final Container target){
    synchronized(target.getTreeLock()){
        final Insets insets=target.getInsets();

        final Rectangle availableBounds = new Rectangle( insets.left, insets.top,
                target.getWidth()- insets.left - insets.right, target.getHeight() - insets.top - insets.bottom);
        final Dimension northSize=sumHorizontal(northComponents,getPreferredSize);
        final Dimension southSize=sumHorizontal(southComponents,getPreferredSize);
        final Dimension westSize=sumVertical(westComponents,getPreferredSize);
        final Dimension eastSize=sumVertical(eastComponents,getPreferredSize);

        int left,right,top,bottom;

        left=availableBounds.x;
        top=availableBounds.y;
        for(final Component c:northComponents){
            if(c.isVisible()){
                final Dimension d=sumHorizontal(asList(c),getPreferredSize);
                c.setBounds(left,top,d.width,d.height);
                left+=d.width;
            }
        }

        left=availableBounds.x;
        bottom=availableBounds.y + availableBounds.height;
        for(final Component c:southComponents){
            if(c.isVisible()){
                final Dimension d=sumHorizontal(asList(c),getPreferredSize);
                c.setBounds(left,bottom - d.height,d.width,d.height);
                left+=d.width;
            }
        }

        left=availableBounds.x;
        top=availableBounds.y + northSize.height;
        for(final Component c:westComponents){
            if(c.isVisible()){
                final Dimension d=sumVertical(asList(c),getPreferredSize);
                c.setBounds(left,top,d.width,d.height);
                top+=d.height;
            }
        }

        right=availableBounds.x + availableBounds.width;
        top=availableBounds.y + northSize.height;
        for(final Component c:eastComponents){
            if(c.isVisible()){
                final Dimension d=sumVertical(asList(c),getPreferredSize);
                c.setBounds(right - d.width,top,d.width,d.height);
                top+=d.height;
            }
        }

        top=availableBounds.x + northSize.height;
        left=availableBounds.y + westSize.width;
        right=availableBounds.x + availableBounds.width - eastSize.width;
        bottom=availableBounds.y + availableBounds.height - southSize.height;
        for(final Component c:centerComponents){
            if(c.isVisible()){
                c.setBounds(left,top,right - left,bottom - top);
            }
        }
    }
 }
  
 private Dimension sumHorizontal(final Iterable<Component> components,
                                 final DimensionGetter getter){
    final Dimension dim=new Dimension();
    for(final Component c:components){
        if(c.isVisible()){
            final Dimension d=getter.get(c);
            dim.width+=d.width + getHgap();
            dim.height=Math.max(d.height,dim.height);
        }
    }
    return dim;
 }
  
 private Dimension sumVertical(final Iterable<Component> components,
                               final DimensionGetter getter){
    final Dimension dim=new Dimension();
    for(final Component c:components){
        if(c.isVisible()){
            final Dimension d=getter.get(c);
            dim.width=Math.max(d.width,dim.width);
            dim.height+=d.height + getVgap();
        }
    }
    return dim;
 }
  
 private Dimension sumCenter(final Iterable<Component> components,
                             final DimensionGetter getter){
    final Dimension dim=new Dimension();
    for(final Component c:components){
      if(c.isVisible()){
          final Dimension d=getter.get(c);
          dim.width+=d.width;
          dim.height=Math.max(d.height,dim.height);
      }
    }
    return dim;
 }
  
 private int max(final int...values){
    int max=0;
    for(int value:values){
        max=Math.max(max,value);
    }
    return max;
   }
}

