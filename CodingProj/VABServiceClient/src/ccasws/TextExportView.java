package ccasws;

import java.io.IOException;
import java.io.Writer;

//import javax.servlet.jsp.JspException;


public interface TextExportView extends ExportView
{

    void doExport(Writer out) throws IOException;//, JspException;

//    boolean outputPage();
}
