/*
 * All rights reserved. This program and the accompanying materials
 * Copyright (c) 2008 committers of openArchitectureWare and others.
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    committers of openArchitectureWare - initial API and implementation
 */

package org.eclipse.emf.mwe.ui.editor.format;

/**
 * @author Patrick Schoenbach
 * @version $Revision: 1.1 $
 */
public class TextFormattingStrategy extends DefaultFormattingStrategy {

    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");

    public TextFormattingStrategy() {
        super();
    }

    @Override
    public String format(final String content, final boolean isLineStart,
            final String indentation, final int[] positions) {
        if (indentation.length() == 0)
            return content;

        return TextFormattingStrategy.LINE_SEPARATOR + content.trim()
                + TextFormattingStrategy.LINE_SEPARATOR + indentation;
    }

}