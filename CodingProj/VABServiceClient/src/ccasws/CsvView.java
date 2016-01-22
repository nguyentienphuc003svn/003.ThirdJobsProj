/**
 * Licensed under the Artistic License; you may not use this file
 * except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://displaytag.sourceforge.net/license.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
 package ccasws;
import java.util.Vector;

/**
 * Export view for comma separated value exporting.
 * @author Fabrizio Giustina
 * @version $Revision: 1.13 $ ($Author: fgiust $)
 */
public class CsvView extends BaseExportView
{

    /**
     * @see org.displaytag.export.BaseExportView#setParameters(TableModel, boolean, boolean, boolean)
     */
    public void setParameters(Vector vecHead,Vector vecBody)
    {
    	super.setParameters(vecHead,vecBody); 
    }

    /**
     * @see org.displaytag.export.BaseExportView#getRowEnd()
     */
    protected String getRowEnd()
    {
        return "\n"; //$NON-NLS-1$
    }

    /**
     * @see org.displaytag.export.BaseExportView#getCellEnd()
     */
    protected String getCellEnd()
    {
        return ","; //$NON-NLS-1$
    }

    /**
     * @see org.displaytag.export.BaseExportView#getAlwaysAppendCellEnd()
     */
    protected boolean getAlwaysAppendCellEnd()
    {
        return false;
    }

    /**
     * @see org.displaytag.export.BaseExportView#getAlwaysAppendRowEnd()
     */
    protected boolean getAlwaysAppendRowEnd()
    {
        return true;
    }

    /**
     * @see org.displaytag.export.ExportView#getMimeType()
     */
    public String getMimeType()
    {
        return "text/csv"; //$NON-NLS-1$
    }

    /**
     * Escaping for csv format.
     * <ul>
     * <li>Quotes inside quoted strings are escaped with a /</li>
     * <li>Fields containings newlines or , are surrounded by "</li>
     * </ul>
     * Note this is the standard CVS format and it's not handled well by excel.
     * @see org.displaytag.export.BaseExportView#escapeColumnValue(java.lang.Object)
     */
    protected String escapeColumnValue(Object value)
    {
        if (value != null)
        {
            String stringValue = trim(value.toString());
            if (!containsNone(stringValue, new char[]{'\n', ','}))
            {
                return "\"" + //$NON-NLS-1$
                    replace(stringValue, "\"", "\\\"") + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }

            return stringValue;
        }

        return null;
    }

}
