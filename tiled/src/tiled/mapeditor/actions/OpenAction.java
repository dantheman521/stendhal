/***************************************************************************
 *                      (C) Copyright 2005 - Marauroa                      *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package tiled.mapeditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import tiled.mapeditor.MapEditor;

/**
 * This action loads a new file
 * 
 * @author mtotz
 */
public class OpenAction extends AbstractAction
{
  private static final long serialVersionUID = 2453067107619628364L;

  private MapEditor mapEditor;
  
  public OpenAction(MapEditor mapEditor)
  {
    super("Open...");
    putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
    putValue(SHORT_DESCRIPTION, "Opens a new file");
    this.mapEditor = mapEditor;
  }

  public void actionPerformed(ActionEvent e)
  {
    mapEditor.openMap();
  }

}
