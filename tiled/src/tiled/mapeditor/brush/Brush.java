/*
 *  Tiled Map Editor, (c) 2004
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 * 
 *  Adam Turk <aturk@biggeruniverse.com>
 *  Bjorn Lindeijer <b.lindeijer@xs4all.nl>
 *  
 *  modified for stendhal, an Arianne powered RPG 
 *  (http://arianne.sf.net)
 *
 *  Matthias Totz <mtotz@users.sourceforge.net>
 */

package tiled.mapeditor.brush;

import java.awt.Graphics;
import java.awt.Rectangle;

import tiled.core.MultilayerPlane;


public interface Brush 
{
    /**
     * This will set the number of layers to affect, the default is 1 - the
     * layer specified in commitPaint.
     * 
     * @see Brush#commitPaint(MultilayerPlane, int, int, int)
     * @param num   the number of layers to affect.
     */
    public void setAffectedLayers(int num);
    
    public int getAffectedLayers();
    
    public Rectangle getBounds();
    
    /**
     * This is the main processing method for a Brush object. Painting starts
     * on initLayer, and if the brush has more than one layer, then the brush  
     * will paint deeper into the layer stack.
     * 
     * @see MultilayerPlane
     * @param mp         The MultilayerPlane to be affected
     * @param x          The x-coordinate where the user initiated the paint
     * @param y          The y-coordinate where the user initiated the paint
     * @param initLayer  The first layer to paint to.
     * @return The rectangular region affected by the painting  
     */
    public Rectangle commitPaint(MultilayerPlane mp, int x, int y,int initLayer);
    
    public void paint(Graphics g, int x, int y);
    
    public boolean equals(Brush b);
}
