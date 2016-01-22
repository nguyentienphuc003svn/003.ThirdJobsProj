package ccasws;

import java.awt.Color;
import java.io.FileOutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class PDFPage extends PdfPageEventHelper {

	/** An Image that goes in the header. */
	public Image headerImage;
	/** The headertable. */
	public PdfPTable table;
	/** The Graphic state */
	public PdfGState gstate;
	/** A template that will hold the total number of pages. */
	public PdfTemplate tpl;
	/** The font that will be used. */
	public BaseFont helv;
	String	watermark="";
	String	logofile=null;

	/**
	 * Generates a document with a header containing Page x of y and with a Watermark on every page.
	 * @param args no arguments needed
	 */
	public static void main(String args[]) {
		try {
			// step 1: creating the document
			Document doc = new Document(PageSize.A4, 50, 50, 100, 72);
			// step 2: creating the writer
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("pageNumbersWatermark.pdf"));
			// step 3: initialisations + opening the document
			writer.setPageEvent(new PDFPage());
			doc.open();
			// step 4: adding content
			String text = "some padding text ";
			for (int k = 0; k < 10; ++k)
				text += text;
			Paragraph p = new Paragraph(text);
			p.setAlignment(Element.ALIGN_JUSTIFIED);
			doc.add(p);
			// step 5: closing the document
			doc.close();
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public PDFPage() {
		this.watermark="GRGBanking";

	}
	public PDFPage(String watermark) {
		if	(watermark!=null && watermark.length()>0)	this.watermark=watermark;

	}
	public void setLogofile(String logofile) {
		if	(logofile!=null && logofile.length()>0)		this.logofile=logofile;
		//CLog.writeLog("logofile="+this.logofile);
	}
	public void setWatermark(String watermark) {
		if	(watermark!=null && watermark.length()>0)	this.watermark=watermark;
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onOpenDocument(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
		try {
			tpl = writer.getDirectContent().createTemplate(100, 100);
			tpl.setBoundingBox(new Rectangle(-20, -20, 100, 100));
			// initialization of the font
			helv = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
		}
		catch(Exception e) {
			throw new ExceptionConverter(e);
		}
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {

		//if	(writer.getPageNumber() > 0 && logofile!=null) {
		if	(logofile!=null) {
			try {
				com.lowagie.text.Image img = com.lowagie.text.Image.getInstance(java.net.URLDecoder.decode(logofile));
				//com.lowagie.text.Image img = com.lowagie.text.Image.getInstance(java.net.URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource(".").getPath())+".."+java.io.File.separator+"webapps"+java.io.File.separator+"ROOT"+java.io.File.separator+"grg"+java.io.File.separator+"images"+java.io.File.separator+"bank.gif");
				//img.setAbsolutePosition(40,6+document.getPageSize().getHeight()-img.getScaledHeight());
				img.setAbsolutePosition(40,img.getScaledHeight()/2);
				//CLog.writeLog("H:"+img.getScaledHeight()+",W:"+img.getScaledWidth());
				img.scalePercent((float)(50*48.0/img.getScaledHeight()));
				//cb.addImage(img);
				document.add(img);
			} catch ( Exception e ) {
				CLog.writeLog(e.toString());
			}	catch	(Throwable a ) {
				CLog.writeLog(a.toString());
			}
		}
/**/
		try {
			PdfContentByte cb = writer.getDirectContent();
			//cb.reset(false);
			cb.saveState();
			cb.beginText();
			cb.setFontAndSize(helv, 12);
			// for odd pagenumbers, show t

			//String textPage = "Page " + writer.getPageNumber() + " of ";
			String textPage = writer.getPageNumber() + " / ";
			float textSize = helv.getWidthPoint(textPage, 12);
			float textBase = document.bottom();
			cb.setTextMatrix((document.right() - document.left())/2 + textSize, textBase);
			cb.showText(textPage);
			cb.endText();
			cb.addTemplate(tpl, (document.right() - document.left())/2 + 2*textSize, textBase);
			cb.saveState();
			gstate = new PdfGState();
			gstate.setFillOpacity(0.3f);
			gstate.setStrokeOpacity(0.3f);
			if	(writer.getPageNumber() >0 && watermark!=null && watermark.length()>0) {
				cb.setGState(gstate);
				cb.setColorFill(Color.blue);
				cb.beginText();
				cb.setFontAndSize(helv, 48);
				//cb.showTextAligned(Element.ALIGN_CENTER, watermark+" " + writer.getPageNumber(), document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 45);
				cb.showTextAligned(Element.ALIGN_CENTER, watermark, document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 45);
				cb.endText();
			}
		}	catch	(Exception e ) {
			CLog.writeLog(e.toString());
		}	catch	(Throwable a ) {
			CLog.writeLog(a.toString());
		}


	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onStartPage(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
	public void onStartPage(PdfWriter writer, Document document) {
		if	(writer.getPageNumber() > 0 && watermark!=null && watermark.length()>0) {
			PdfContentByte cb = writer.getDirectContentUnder();
			cb.saveState();
			cb.setColorFill(Color.blue);
			cb.beginText();
			cb.setFontAndSize(helv, 48);
			cb.showTextAligned(Element.ALIGN_CENTER, watermark+" " + writer.getPageNumber(), document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 45);
			cb.endText();
			cb.restoreState();
		}
	}
	 */

	/**
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onCloseDocument(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
	 */
	public void onCloseDocument(PdfWriter writer, Document document) {
		tpl.beginText();
		tpl.setFontAndSize(helv, 12);
		tpl.setTextMatrix(0, 0);
		tpl.showText(Integer.toString(writer.getPageNumber() - 1));
		tpl.endText();
	}

}
