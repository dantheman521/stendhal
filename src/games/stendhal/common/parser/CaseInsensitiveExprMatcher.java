/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2024 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.common.parser;

/**
 * CaseInsensitiveExprMatcher creates an ExpressionMatcher with exact and case insensitive matching.
 *
 * @author Martin Fuchs
 */
public final class CaseInsensitiveExprMatcher extends ExpressionMatcher {

	public CaseInsensitiveExprMatcher() {
		exactMatching = true;
		caseInsensitive = true;
	}

}
