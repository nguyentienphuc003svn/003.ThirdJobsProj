package ccas;

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.*;
import java.math.*;

import java.awt.image.BufferedImage;
import javax.imageio.*;

//import java.text.DecimalFormat;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import java.util.Iterator;
import java.util.Vector;

public class PoiExcelView implements BinaryExportView
{
	private Vector vecHead;
	private Vector vecBody;
	private Vector vecTail;
	private Vector vecRow;
	private boolean landscape=false;
	private boolean protect=false;
	//private boolean exportFull;
	//private boolean decorated;
	private HSSFWorkbook workbook=null;

	private HSSFSheet		sheet=null;
	private HSSFHeader 	hssfHeader=null;
	private HSSFFooter 	hssfFooter=null;
	private HSSFRow 		hssfRow=null;
	private HSSFCell		hssfCell=null;
	private	HSSFCellStyle cellStyle=null;
	private int		colSize=0;
	private int		rowNum=0;
	private int		colNum=0,maxColNum=0;
	private int		colWidth[][];

	private String picturefile=null;
	private int col1,row1,col2,row2;

	public void init() throws Exception	{
		colSize	= 0;
		rowNum	= 0;
		colNum	= 0;
		vecHead = new Vector();
		vecBody = new Vector();
		vecTail = new Vector();
		vecRow 	= new Vector();
		colWidth= null;
		workbook= new HSSFWorkbook();
		cellStyle= workbook.createCellStyle();
		// 1.如果需要前景颜色或背景颜色，一定要指定填充方式，两者顺序无所谓；
		// 2.如果同时存在前景颜色和背景颜色，前景颜色的设置要写在前面；
		// 3.前景颜色不是字体颜色。
		//cellStyle.setFillPattern(HSSFCellStyle.DIAMONDS);
		cellStyle.setFillForegroundColor(HSSFColor.RED.index);
		cellStyle.setFillBackgroundColor(HSSFColor.LIGHT_YELLOW.index);
		//水平布局：居中
		//cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//cellStyle.setWrapText(true);
		// 设置单元格底部的边框及其样式和颜色
		// 这里仅设置了底边边框，左边框、右边框和顶边框同理可设
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		//cellStyle.setDataFormat((short)1);

		sheet		= workbook.createSheet();
		hssfRow = sheet.createRow((short) rowNum);

		hssfHeader=sheet.getHeader();
		hssfFooter=sheet.getFooter();
		//hssfFooter.setRight( "Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages() );
		hssfFooter.setCenter("Page " + HSSFFooter.page() + " of  " + HSSFFooter.numPages() );

	}

	public void PoiExcelView()	{
		vecHead = new Vector();
		vecBody = new Vector();
		vecTail = new Vector();
		vecRow 	= new Vector();
		colWidth = null;

	}

	public void addTail(String filed)	{
		vecTail.add(filed);
	}

	public void addTail(int filed)	{
		//addTail(new Integer(filed).toString());
		addTail((Integer)filed);
	}

	public void addTail(double filed)	{
		//addTail(App.DAmount2S(filed));
		addTail((Double)filed);
	}

	public void addHead(String filed)	{
		vecHead.add(filed);
		setSheetName(0,filed.substring(0,Math.min(30,(filed+"(").indexOf("("))));
	}

	public void addHead(int filed)	{
		//addHead(new Integer(filed).toString());
		addHead((Integer)filed);
	}

	public void addHead(double filed)	{
		//addHead(App.DAmount2S(filed));
		addHead((Double)filed);
	}

	public void addRow()	{
		vecBody.add(vecRow);
		vecRow = new Vector();
	}

	public void addRowField(Object rowField)	{
		String ret=null;
		if	(rowField instanceof String)		ret=(String)rowField;

		if	(rowField instanceof Integer)		ret=String.format("%d",((Integer)rowField).intValue());
		if	(rowField instanceof Long)			ret=String.format("%d",((Long)rowField).longValue());
		if	(rowField instanceof Double)		ret=String.format("%.f",((Double)rowField).doubleValue());
		if	(rowField instanceof Float)			ret=String.format("%.f",((Float)rowField).floatValue());
		if	(rowField instanceof Boolean){
			if	(((Boolean)rowField).booleanValue())	ret="true";	else	ret="false";
		}
		if	(rowField instanceof Date)			ret=(new SimpleDateFormat("yyyy/MM/dd")).format((Date)rowField);
		if	(ret==null)	ret=(String)rowField;

		vecRow.add(ret);
	}

	public void addRowField(String rowField)	{
		vecRow.add(rowField);
	}

	public void addRowField(int rowField)	{
		//vecRow.add(new Integer(rowField).toString());
		vecRow.add((Integer)rowField);
	}

	public void addRowField(double rowField)
	{
		//vecRow.add(App.DAmount2S(rowField));
		vecRow.add((Double)rowField);
	}

	public void setParameters(Vector vecHead,Vector vecBody)	{
		this.vecHead = vecHead;
		this.vecBody = vecBody;
	}

	public String getMimeType()	{
		return "application/vnd.ms-excel"; //$NON-NLS-1$
	}


	protected void generateExcel()	{
		generateHeaders();
		generateRows();
	}

	public void setSheetName(int num,String sheetname)	{
		if	(sheetname==null || sheetname.length()==0)	return;
		workbook.setSheetName(num,sheetname);
		//workbook.setSheetName(num,sheetname,HSSFWorkbook.ENCODING_UTF_16);
		//workbook.setSheetName(num,sheetname,(short)1);
	}
	public void setHeight(short height)	{
		hssfRow.setHeight(height);
	}
	public void setHeight(int height)	{
		hssfRow.setHeight((short)height);
	}
	public void setHeightInPoints(int height)	{
		hssfRow.setHeightInPoints((float)height);
	}
	public void setHeightInPoints(float height)	{
		hssfRow.setHeightInPoints(height);
	}
	public void setColWidth(int [][] cw)	{
		colWidth = cw;
	}
	public void setAllColWidth(){
		HSSFPrintSetup ps = sheet.getPrintSetup();
		double percent=(9.0/(double)Math.max(maxColNum,6))*100.0;
		//CLog.writeLog("percent="+percent+",maxColNum="+maxColNum);
		sheet.setZoom(Math.max(75,(int)(percent)),100);   // percent magnification
		sheet.setAutobreaks(true);
		sheet.setSelected(true);
		//sheet.setVerticallyCenter(true);
		sheet.setHorizontallyCenter(true);
		sheet.setDisplayGridlines(false);
		sheet.setDisplayGuts(true);
		//sheet.setFitToPage(true);

		ps.setFitHeight((short)(1+rowNum/25));
		ps.setFitWidth((short)1);
		//ps.setNoOrientation(false);
		//将页面设置为横向打印模式
		if	(maxColNum>9)								ps.setLandscape(true);
		if	(rowNum<25 && maxColNum>5)	ps.setLandscape(true);
		if	(landscape)									ps.setLandscape(landscape);
		//if	(protect)										sheet.setProtect(protect);

		//自动调整宽度
		for	(int i=0;i<maxColNum*2;i++)		sheet.autoSizeColumn((short)i);
		if (colWidth == null){
			//sheet.setColumnWidth((short)0,(short)(256*6));
			return;
		}
		int num = colWidth.length;
		for (int i = 0; i < num; i++) {
			if (sheet != null)
			sheet.setColumnWidth((short)colWidth[i][0],(short)(256*colWidth[i][1]));
		}
	}

	public void setLandscape(boolean landscape) throws Exception	{
		this.landscape=landscape;
	}

	public void setProtect(boolean protect) throws Exception	{
		this.protect=protect;
	}

	public void doExport(OutputStream out) throws Exception	{
		try		{
			// Initialize the table with the appropriate number of columns
			//initTable();
			generateExcel();
			setAllColWidth();
			//loadPicture();
			workbook.write(out);
			out.flush();
		}	catch (Exception e)	{
			throw e;
			//throw new PdfGenerationException(e);
		}
	}

	public void loadPicture(String picturefile,int col1,int row1){
		this.picturefile=picturefile;
		this.col1=col1;
		this.row1=row1;
		this.col2=col1;
		this.row2=row1+1;
		loadPicture();
	}
	public void loadPicture(String picturefile,int col1,int row1,int col2,int row2){
		this.picturefile=picturefile;
		this.col1=col1;
		this.row1=row1;
		this.col2=col2;
		this.row2=row2;
		loadPicture();
	}
	private int loadPicture(){
		int retHeight=-1;
		if	(picturefile==null)	return retHeight;
		int width=sheet.getColumnWidth((short)col1)/37;
		//CLog.writeLog("width="+width);
		String filetype=picturefile.substring(picturefile.lastIndexOf(".")+1).toUpperCase();
		try		{
			int fileformat=HSSFWorkbook.PICTURE_TYPE_PNG;
			if	(filetype.equals("JPG") || filetype.equals("JPEG"))	fileformat=HSSFWorkbook.PICTURE_TYPE_JPEG;
			if	(filetype.equals("PNG") || filetype.equals("GIF"))	fileformat=HSSFWorkbook.PICTURE_TYPE_PNG;
			if	(fileformat==HSSFWorkbook.PICTURE_TYPE_PNG)	filetype="PNG";
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

			//BufferedImage bufferImg = ImageIO.read(new File(picturefile.replaceAll("/","\\\\")));
			BufferedImage bufferImg = ImageIO.read(new File(picturefile));
			ImageIO.write(bufferImg,filetype,byteArrayOut);
			if	(bufferImg.getWidth()>width && col1==col2)	col2++;
			retHeight=bufferImg.getHeight();
			HSSFClientAnchor anchor =new HSSFClientAnchor(1,0,bufferImg.getWidth()+1,bufferImg.getHeight(),(short)col1,row1,(short)col2,row2);
			HSSFPatriarch patri = sheet.createDrawingPatriarch();
			patri.createPicture(anchor,
				workbook.addPicture(byteArrayOut.toByteArray(),
				fileformat));
			//CLog.writeLog("picturefile="+picturefile.replaceAll("/","\\\\")+",filetype="+filetype+","+bufferImg.getWidth()+"X"+retHeight);
		}	catch (Exception e)	{
			CLog.writeLog("picturefile="+picturefile+",filetype="+filetype+","+retHeight);
			CLog.writeLog(e.toString());
			//throw e;
		}	finally	{
			filetype=null;
		}
			return retHeight;
	}


	protected void generateHeaders()	{
		if (vecHead.size()>0)
		{
			//hssfRow = sheet.createRow((short) rowNum);
			Iterator iterator = vecHead.iterator();

			while (iterator.hasNext())
			{
				//hssfCell = hssfRow.createCell((short) colNum);
				//hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);//
				String columnHeader = (String)iterator.next();
				//hssfCell.setCellValue(trimToEmpty(columnHeader));
				hssfHeader.setCenter(trimToEmpty(columnHeader));
				//colNum++;
			}
			//rowNum++;
			colNum=0;
		}
		//CLog.writeLog("vecTail.size="+vecTail.size());
		if (vecTail.size()>0)
		{
			//hssfRow = sheet.createRow((short) rowNum);
			Iterator it = vecTail.iterator();

			if (it.hasNext())
			{
				hssfFooter.setLeft(trimToEmpty(it.next()));
				if	(it.hasNext())	hssfFooter.setRight(trimToEmpty(it.next()));

			}
		}

	}




	protected void generateRows()	{

		Iterator rowIterator = vecBody.iterator();
		// iterator on rows
		for	(int i=0;rowIterator.hasNext();i++)
		{
			hssfRow = sheet.createRow((short) rowNum);
			//if	(rowNum==0)	hssfRow.setHeight((short)(hssfRow.getHeight()*1.5));
			Vector row = (Vector)rowIterator.next();

			// iterator on columns
			Iterator columnIterator = row.iterator();

			HSSFCellStyle cellStyle0=cellStyle;
			while (columnIterator.hasNext())
			{

				hssfCell = hssfRow.createCell((short) colNum);
				//HSSFCell.ENCODING_UTF_16;
				//hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);//
				//String colValue = trimToEmpty((String)columnIterator.next());
				//hssfCell.setCellValue(colValue);
				//hssfCell.setCellValue(trimToEmpty(columnIterator.next()));
				Object colValue = columnIterator.next();
				if	(rowNum<1 || (colValue.toString().trim().length()<=0 && colNum<=0))		cellStyle0= workbook.createCellStyle();

				//if	(colValue instanceof Double || colValue instanceof Integer)	try	{hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);}	catch	(Exception e)	{}
				//if	(colValue instanceof String)	try	{hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);}	catch	(Exception e)	{}
				colNum++;
				//if	(colValue instanceof Double)	cellStyle0.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));   ;
				//if	(colValue instanceof Integer)	cellStyle0.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));   ;
				//hssfCell.setCellValue(trimToEmpty(colValue)+hssfCell.getCellType());
				hssfCell.setCellValue(trimToEmpty(colValue));
				hssfCell.setCellStyle(cellStyle0);

			}

			if	(maxColNum<colNum)	maxColNum=colNum;
			rowNum++;
			colNum=0;
		}
	}

	public static String trimToEmpty(String str) {
		return str == null ? "" : str.trim();
	}

	public static String trimToEmpty(Object o) {
		return o.toString().trim();
	}

	public static int trimToEmpty(int c) {
		return c;
	}

	public static double trimToEmpty(double c) {
		return c;
	}



}
