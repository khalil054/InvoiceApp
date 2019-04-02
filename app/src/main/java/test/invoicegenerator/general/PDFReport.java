/*
package test.invoicegenerator.general;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

*/
/**
 * Created by User on 9/17/2018.
 *//*


public class PDFReport {
    private Context context;
    private File pdfFolder;

    public PDFReport(Context con)
    {
        context=con;
    }
    public void init_pdf_creation() throws IOException, DocumentException {
        try {
            Util.showToastAlpha("Generating pdf...",context);
            //create_pdf_file();
            convertHtmlToPdf();
            Util.showToastAlpha("PDF generated successfully",context);
        } catch (FileNotFoundException e) {
            Log.e("PDF Error ", "File not Found exception");
        }
    }
    public void create_pdf_file() throws FileNotFoundException {
        pdfFolder = new File(Environment.getExternalStorageDirectory(), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i("Test321", "PDF directory created");
        }
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        File myFile = new File(pdfFolder + "/" + "noor" + ".pdf");

        // File myFile = new File(pdfFolder + "/" + "5S Report" + ".pdf");
        //  OutputStream output = new FileOutputStream(myFile);
        Document document = new Document(PageSize.LEGAL);
        if (myFile.exists ())
            myFile.delete ();
        try {
            FileOutputStream out = new FileOutputStream(myFile);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(myFile));
            //  PdfWriter writer = PdfWriter.getInstance(document, output);
            document.open();
            addClientTable(document);
            Font f = new Font(Font.FontFamily.TIMES_ROMAN, 21.0f, Font.BOLD, BaseColor.BLACK);
            Chunk c = new Chunk("Invoice generating");
            Paragraph paragraph = new Paragraph(c);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            // create_top_paragraph(document);
            //create_lower_table(document);
            // add_action_item(document);
            //Separate questions according to category
            PdfPTable table = new PdfPTable(8);
            PdfPCell cell = new PdfPCell(new Phrase("Receipt"));
            cell.setColspan(3);
            cell.setPadding(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.setSpacingBefore(20);
            table.addCell(cell);

            document.close();

            //Open PDF
            open_pdf_file(myFile);
        } catch (Exception ex) {
            Log.e("Test321", "Exception = " + ex.getMessage());
        }
    }

    private void addClientTable(Document document) {
        PdfPTable table = new PdfPTable(8);
        PdfPCell cell = new PdfPCell(new Phrase("Receipt"));
        cell.setColspan(3);
        cell.setPadding(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.setSpacingBefore(20);
        table.addCell(cell);
        //Date label
        PdfPCell cell2 = new PdfPCell(new Phrase("Date"));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setPadding(10);
        table.addCell(cell2);
    }

    private void open_pdf_file(File file) throws IOException, DocumentException {


        try {
            */
/*Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(
                    Uri.fromFile(file),
                    "application/pdf");*//*

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(
                    Uri.fromFile( file),
                    "application/pdf");
            context.startActivity(intent);
        } catch (Exception ex) {
            Util.showAlertBox("No Application found to open this application.", "Sorry", context, false);
        }

    }
    public void compressPdf(String src, String dest) throws IOException, DocumentException {

        PdfReader reader = new PdfReader(src);

        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest), PdfWriter.VERSION_1_5);

        stamper.getWriter().setCompressionLevel(9);

        int total = reader.getNumberOfPages() + 1;

        for (int i = 1; i < total; i++) {

            reader.setPageContent(i, reader.getPageContent(i));

        }

        stamper.setFullCompression();

        stamper.close();

    }
    public void createPdf() {
        // TODO Auto-generated method stub
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        File file = null;

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/vindroid";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);


            file = new File(dir, "sample.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(document, fOut);

            //open the document
            document.open();


            Paragraph p1 = new Paragraph("Sample PDF CREATION USING IText");
            Font paraFont= new Font(Font.FontFamily.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //add paragraph to document
            document.add(p1);

            Paragraph p2 = new Paragraph("This is an example of a simple paragraph");
            Font paraFont2= new Font(Font.FontFamily.COURIER,14.0f,0, CMYKColor.GREEN);
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setFont(paraFont2);

            document.add(p2);

            */
/*ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_background);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.MIDDLE);*//*


            //add image to document
            // document.add(myImg);
            // generateTable(document);
            //convertHtmlToPdf();
            //  generteSampleTable(document);
            open_pdf_file(file);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally
        {
            document.close();
        }

    }
    public boolean createPDF(String rawHTML, String fileName, ContextWrapper context) {
        final String APPLICATION_PACKAGE_NAME = context.getBaseContext().getPackageName();
        File path = new File(Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME);
        if (!path.exists()) {
            path.mkdir();
        }
        File file = new File(path, fileName);

        try {

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Подготавливаем HTML
            String htmlText = Jsoup.clean(rawHTML, Whitelist.relaxed());
            InputStream inputStream = new ByteArrayInputStream(htmlText.getBytes());

            // Печатаем документ PDF
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    inputStream, null, Charset.defaultCharset());

            document.close();
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void generteSampleTable(Document document) {
        PdfPTable table = new PdfPTable(8);
        PdfPCell cell = new PdfPCell(new Phrase("Receipt"));
        cell.setColspan(3);
        cell.setPadding(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.setSpacingBefore(20);
        table.addCell(cell);
    }

    private void generateTable(Document document) throws DocumentException {

        PdfPTable table = new PdfPTable(8);
        PdfPCell cell = new PdfPCell(new Phrase("Receipt"));
        cell.setColspan(3);
        cell.setPadding(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.setSpacingBefore(20);
        table.addCell(cell);
        //Date label
        PdfPCell cell2 = new PdfPCell(new Phrase("Date"));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setPadding(10);
        table.addCell(cell2);
        //Date Value
        Chunk subscript = new Chunk("---------");
        subscript.setTextRise(-10f);
        cell2.setPadding(7);
        PdfPCell cell3 = new PdfPCell(new Phrase(subscript));

        cell3.setColspan(2);
        cell3.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell3);
        //serial number
        PdfPCell cell4 = new PdfPCell(new Phrase("0001"));
        cell4.setColspan(2);
        cell2.setPadding(10);
        cell4.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell4);

        //2nd row
        PdfPCell row2cell1 = new PdfPCell(new Phrase("Received from"));
        row2cell1.setColspan(3);
        row2cell1.setPadding(10);
        row2cell1.setBorder(Rectangle.NO_BORDER);
        table.addCell(row2cell1);
        //2nd row cell 2
        PdfPCell row2cell2 = new PdfPCell(new Phrase("----------------------------------"));
        row2cell2.setColspan(5);
        row2cell2.setPadding(10);
        row2cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(row2cell2);

        //3rd row
        PdfPCell row3cell1 = new PdfPCell(new Phrase("Payment for "));
        row3cell1.setColspan(3);
        row3cell1.setPadding(10);
        row3cell1.setBorder(Rectangle.NO_BORDER);
        table.addCell(row3cell1);
        //2nd row cell 2//
        Chunk payment = new Chunk("----------------------------------");
        subscript.setTextRise(-10f);
        PdfPCell row3cell2 = new PdfPCell(new Phrase(payment));
        row3cell2.setColspan(5);
        row2cell2.setPadding(10);
        row3cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(row3cell2);
       */
/* for(int aw = 0; aw < 14; aw++){
            table.addCell("hi");
        }*//*

        document.add(table);
    }
    private void convertHtmlToPdf() throws IOException, DocumentException {


        // itextpdf-5.4.1.jar  http://sourceforge.net/projects/itext/files/iText/
        // xmlworker-5.4.1.jar http://sourceforge.net/projects/xmlworker/files/

             */
/*   try {
                    Document document = new Document(PageSize.LETTER);
                    PdfWriter pdfWriter = PdfWriter.getInstance
                            (document, new FileOutputStream("c://temp//testpdf.pdf"));
                    document.open();
                    document.addAuthor("Real Gagnon");
                    document.addCreator("Real's HowTo");
                    document.addSubject("Thanks for your support");
                    document.addCreationDate();
                    document.addTitle("Please read this");

                    XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

                    String str = "<html><head></head><body>"+
                            "<a href='http://www.rgagnon.com/howto.html'><b>Real's HowTo</b></a>" +
                            "<h1>Show your support</h1>" +
                            "<p>It DOES cost a lot to produce this site - in ISP storage and transfer fees, " +
                            "in personal hardware and software costs to set up test environments, and above all," +
                            "the huge amounts of time it takes for one person to design and write the actual content.</p>" +
                            "<p>If you feel that effort has been useful to you, perhaps you will consider giving something back?</p>" +
                            "<p>Donate using PayPal� to real@rgagnon.com.</p>" +
                            "<p>Contributions via PayPal are accepted in any amount</p>" +
                            "<P><br/><table border='1'><tr><td>Java HowTo</td></tr><tr>" +
                            "<td style='background-color:red;'>Javascript HowTo</td></tr>" +
                            "<tr><td>Powerbuilder HowTo</td></tr></table></p>" +
                            "</body></html>";
                    worker.parseXHtml(pdfWriter, document, new StringReader(str));
                    document.close();
                    System.out.println("Done.");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }*//*

// step 1
        // Document document = new Document();
        // step 2
        // PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("pdf.pdf"));
        // step 3


        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/vindroid";

        File dir = new File(path);
        if(!dir.exists())
            dir.mkdirs();

        Log.d("PDFCreator", "PDF Path: " + path);


        File file = new File(dir, "htmlconversion.pdf");
        */
/*FileOutputStream fOut = new FileOutputStream(file);

        PdfWriter writer =PdfWriter.getInstance(document, fOut);
        document.open();
        // step 4
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream("file:///android_assets/index.html"));

     //   document.add(new Paragraph(new Chunk("Nooooooor00000")));
        //step 5
        document.close();*//*


//...
        try {
            String f="Variable Value";
            String k = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                    "<html>\n" +
                    "\t<head>\n" +
                    "\t\t<title>INV-5 Open</title>\n" +
                    "\t\t<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t</head>\n" +
                    "\t<body>\n" +
                    "\t\t<h1>HTML to PDF</h1>\n" +f+
                    "\t\t<p>\n" +
                    "\t\t\t<span class=\"itext\">" +
                    "<center><table style=\"width:70%;align:center\">\n" +
                    "  <tr>\n" +
                    "    <td align=\"left\">Invoice Date:</td>\n" +
                    "    <td align=\"left\">25 Sep 2018</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td align=\"left\">Due Date:</td>\n" +
                    "    <td align=\"left\">23 Oct 2018</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td align=\"left\">Billing Address:</td>\n" +
                    "    <td align=\"left\">EvolversTech Lahore</td>\n" +
                    "  </tr>\n" +
                    "</table></center></span> <span class=\"description\"> </span>\n" +
                    "\t\t</p>\n" +
                    "\t\t<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"200px\" style=\"border-collapse:collapse;\">\n" +
                    "\t\t  <tr bgcolor=\"#C9E6FA\">\n" +
                    "\t\t\t\t<th class=\"label\">Title</th>\n" +
                    "\t\t\t\t<td>iText - Java HTML to PDF</td>\n" +
                    "\t\t\t</tr>\n" +
                    "\t\t\t<tr>\n" +
                    "\t\t\t\t<th>URL</th>\n" +
                    "\t\t\t\t<td>http://hmkcode.com/itext-html-to-pdf-using-java</td>\n" +
                    "\t\t\t</tr>\n" +
                    "\t\t</table>\n" +
                    "\t</body>\n" +
                    "</html>";

           */
/* InputStream is1 = context.getAssets().open("index.html");
            int size = is1.available();

            byte[] buffer = new byte[size];
            is1.read(buffer);
            is1.close();

            String str = new String(buffer);
            k=str;*//*

            int permsRequestCode = 200;
            if ( ContextCompat.checkSelfPermission( context, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {

                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, permsRequestCode);
            }

            OutputStream file1 = new FileOutputStream(file);
            Document document1 = new Document();
            PdfWriter writer1 = PdfWriter.getInstance(document1, file1);
            document1.open();
            InputStream is = new ByteArrayInputStream(k.getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer1, document1, is);
            document1.close();
            file1.close();
            open_pdf_file(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/
