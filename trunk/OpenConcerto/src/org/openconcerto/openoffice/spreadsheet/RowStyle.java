/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2011 OpenConcerto, by ILM Informatique. All rights reserved.
 * 
 * The contents of this file are subject to the terms of the GNU General Public License Version 3
 * only ("GPL"). You may not use this file except in compliance with the License. You can obtain a
 * copy of the License at http://www.gnu.org/licenses/gpl-3.0.html See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each file.
 */
 
 package org.openconcerto.openoffice.spreadsheet;

import org.openconcerto.openoffice.ODFrame;
import org.openconcerto.openoffice.ODPackage;
import org.openconcerto.openoffice.StyleStyle;
import org.openconcerto.openoffice.StyleStyleDesc;
import org.openconcerto.openoffice.XMLVersion;

import org.jdom.Element;

public class RowStyle extends StyleStyle {

    // from section 18.728 in v1.2-part1
    public static final StyleStyleDesc<RowStyle> DESC = new StyleStyleDesc<RowStyle>(RowStyle.class, XMLVersion.OD, "table-row", "ro", "table") {
        @Override
        public RowStyle create(ODPackage pkg, Element e) {
            return new RowStyle(pkg, e);
        }
    };

    public RowStyle(final ODPackage pkg, Element tableColElem) {
        super(pkg, tableColElem);
    }

    public final float getHeight() {
        return ODFrame.parseLength(getFormattingProperties().getAttributeValue("row-height", this.getSTYLE()), TableStyle.DEFAULT_UNIT);
    }

}
