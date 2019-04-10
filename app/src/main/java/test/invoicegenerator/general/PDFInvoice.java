package test.invoicegenerator.general;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.R;
import test.invoicegenerator.fragments.FragmentEditReportUpdate;
import static test.invoicegenerator.general.Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

public class PDFInvoice extends Fragment {
    private File pdfFolder;
    Font font;
    private PDFView pdfview;
    private ImageView imageView_Dummy;
    private String invoice_name,invoice_date,due_date,client_name="";
    private FloatingActionButton share_pdf;
    Bitmap decodedImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.pdf_report, container, false);
        try {
            init(view);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void init(View view) throws FileNotFoundException {

        Toast.makeText(getActivity(), "loaded pdf fragment", Toast.LENGTH_SHORT).show();

        font = new Font();
        font.setColor(BaseColor.WHITE);
        pdfview = view.findViewById(R.id.pdfView);
        share_pdf=view.findViewById(R.id.share_pdf);
        imageView_Dummy=view.findViewById(R.id.image_dummy);
        showImage();

        initializeInvoice();

      //  create_pdf_file();

        share_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePdfInvoice(getExternalStorageFile());
            }
        });


    }

    private void initializeInvoice() {
       /* client_name="Babar Client Name";
        invoice_name="Babar Invoice_name Name";
        invoice_date="2 Feb, 2019";
        due_date= "2 Feb, 2020";*/


        if(isEmptyString(GlobalData.singleInvoiceDetailModel.getClient_id())){
            Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();

            client_name="Babar Client Name";
            invoice_name="Babar Invoice_name Name";
            invoice_date="2 Feb, 2019";
            due_date= "2 Feb, 2020";
        }else {
            client_name=GlobalData.singleInvoiceDetailModel.getClient_id();
            invoice_name="Invoice #"+GlobalData.singleInvoiceDetailModel.getInvoice_number();
            invoice_date=GlobalData.singleInvoiceDetailModel.getCreated_at();
            due_date= GlobalData.singleInvoiceDetailModel.getDue_at();
        }

    }

    public void create_pdf_file() throws FileNotFoundException {
        File myFile=getExternalStorageFile();

        if(Util.checkPermissionWRITE_EXTERNAL_STORAGE(getActivity())) {
            preparePdfDocument(myFile);
        }
    }
    private File getExternalStorageFile() {
        pdfFolder = new File(Environment.getExternalStorageDirectory(), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
        }
        String pathname = pdfFolder + "/" + "InvoiceGenerator" + ".pdf";
        File myFile = new File(pathname);
        return myFile;
    }
    private PdfPTable addClientTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{1});
        PdfPCell cell = new PdfPCell(new Phrase("Bill To"));
        cell.setPadding(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //Date label
        PdfPCell cell2 = new PdfPCell(new Phrase(""+client_name));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setPadding(10);
        table.addCell(cell2);
        return table;
    }
    private PdfPTable addInvoiceDateDetail() throws DocumentException {
        PdfPTable table = new PdfPTable(new float[] { 2, 4});
        PdfPCell cell = new PdfPCell(new Phrase(""+invoice_name));
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setColspan(2);
        cell.setPadding(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //Date label
        PdfPCell cell2 = new PdfPCell(new Phrase("Date"));
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell2);
        //first row  second column
        PdfPCell row1cell2 = new PdfPCell(new Phrase(""+invoice_date));
        row1cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(row1cell2);

        //Date label
        PdfPCell row2cell1 = new PdfPCell(new Phrase("Due"));
        row2cell1.setBorder(Rectangle.NO_BORDER);
        table.addCell(row2cell1);
        //first row  second column
        PdfPCell row2cell2 = new PdfPCell(new Phrase(""+due_date));
        row2cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(row2cell2);





        return table;
    }
    private void itemDataTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{3, 1, 1, 1});

   /*     for (int i=0; i<= 150; i++){
            String description = "Its testing 1";
            String amount = "100";
            String quantity = String.valueOf(i);
            String unit_cost = "100";
            PdfPCell cell = new PdfPCell(new Phrase(description));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //Date label
            PdfPCell cell2 = new PdfPCell(new Phrase(quantity));
            cell2.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell2);

            //first row  second column
            PdfPCell row1cell2 = new PdfPCell(new Phrase(unit_cost));
            row1cell2.setBorder(Rectangle.NO_BORDER);
            table.addCell(row1cell2);
            //Date label
            PdfPCell row2cell1 = new PdfPCell(new Phrase(amount));
            row2cell1.setBorder(Rectangle.NO_BORDER);

            table.addCell(row2cell1);
        }
*/
        for (int i=0; i<=FragmentEditReportUpdate.item_values.size()-1 ; i++){
            String description = FragmentEditReportUpdate.item_values.get(i).getDescription();
            String amount = FragmentEditReportUpdate.item_values.get(i).getAmount();
            String quantity =  FragmentEditReportUpdate.item_values.get(i).getQuantity();
            String unit_cost =  FragmentEditReportUpdate.item_values.get(i).getUnit_cost();

          //  Toast.makeText(getActivity(), String.valueOf(description+"\n"+amount+"\n"+quantity+"\n"+unit_cost+"\n"), Toast.LENGTH_SHORT).show();
            PdfPCell cell = new PdfPCell(new Phrase(description));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //Date label
            PdfPCell cell2 = new PdfPCell(new Phrase(quantity));
            cell2.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell2);

            //first row  second column
            PdfPCell row1cell2 = new PdfPCell(new Phrase(unit_cost));
            row1cell2.setBorder(Rectangle.NO_BORDER);
            table.addCell(row1cell2);
            //Date label
            PdfPCell row2cell1 = new PdfPCell(new Phrase(amount));
            row2cell1.setBorder(Rectangle.NO_BORDER);

            table.addCell(row2cell1);
        }
        //Toast.makeText(getActivity(), String.valueOf(FragmentEditReportUpdate.item_values.size()), Toast.LENGTH_SHORT).show();


        table.setSpacingBefore(30);
        document.add(table);
    }
    private void calculatingSubtotal(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[] { 3,1,1, 1});
        PdfPCell cell = new PdfPCell(new Phrase("Its comments"));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //Date label
        PdfPCell cell2 = new PdfPCell(new Phrase(""));
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell2);
        //first row  second column
        PdfPCell row1cell2 = new PdfPCell(new Phrase("Subtotal"));
        row1cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(row1cell2);

        //Date label
        PdfPCell row2cell1 = new PdfPCell(new Phrase("Its Subtotal"));
        row2cell1.setBorder(Rectangle.NO_BORDER);
        table.addCell(row2cell1);


        //empty column
        PdfPCell empty_com = new PdfPCell(new Phrase(""));
        empty_com.setColspan(2);
        empty_com.setBorder(Rectangle.NO_BORDER);
        table.addCell(empty_com);
        //discont label column
        PdfPCell dis_col = new PdfPCell(new Phrase("Discount"));
        dis_col.setBorder(Rectangle.NO_BORDER);
        table.addCell(dis_col);
        //discount value column
        PdfPCell dis_value_col = new PdfPCell(new Phrase("Its Discount"));
        dis_value_col.setBorder(Rectangle.NO_BORDER);
        table.addCell(dis_value_col);
        //empty column
        PdfPCell empty_col = new PdfPCell(new Phrase(""));
        empty_col.setColspan(2);
        empty_col.setBorder(Rectangle.NO_BORDER);
        table.addCell(empty_col);
        empty_col.setBorder(Rectangle.BOTTOM);
        PdfPCell tax_col = new PdfPCell(new Phrase("Tax"));
        tax_col.setBorder(Rectangle.NO_BORDER);
        tax_col.setBorder(Rectangle.BOTTOM);
        table.addCell(tax_col);
        //discount value column
        PdfPCell tax_value_col = new PdfPCell(new Phrase("Its Tax"));
        tax_value_col.setBorder(Rectangle.NO_BORDER);
        tax_value_col.setBorder(Rectangle.BOTTOM);
        table.addCell(tax_value_col);
        //empty column
        PdfPCell empty_col2 = new PdfPCell(new Phrase(""));
        empty_col2.setColspan(2);
        empty_col2.setBorder(Rectangle.NO_BORDER);
        table.addCell(empty_col2);

        PdfPCell total_col = new PdfPCell(new Phrase("Total"));
        total_col.setBorder(Rectangle.NO_BORDER);
        table.addCell(total_col);

        //discount value column
        PdfPCell total_value_col = new PdfPCell(new Phrase("Its Total"));
        total_value_col.setBorder(Rectangle.NO_BORDER);
        table.addCell(total_value_col);

        PdfPCell empty_col3 = new PdfPCell(new Phrase(""));
        empty_col3.setColspan(2);
        empty_col3.setBorder(Rectangle.NO_BORDER);
        table.addCell(empty_col3);

        //balance column
        PdfPCell balance_col = new PdfPCell(new Phrase("$Balance"));
        balance_col.setBorder(Rectangle.NO_BORDER);
        // table.addCell(balance_col);

        PdfPCell balance_val_col = new PdfPCell(new Phrase("45.67"));
        balance_val_col.setBorder(Rectangle.NO_BORDER);
        table.setSpacingBefore(30);
        document.add(table);
    }
    private void openPdfPage(File file)
    {
        pdfview.fromFile(file)
                .defaultPage(1)
                .enableSwipe(true)
                .load();
    }
    private void addHeaderTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[] { 3,1,1, 1});

        PdfPCell cell = new PdfPCell(new Phrase("Description",font));
        BaseColor black = WebColors.getRGBColor("#000000");
        cell.setBackgroundColor(black);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //Date label
        PdfPCell cell2 = new PdfPCell(new Phrase("Qty",font));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setBackgroundColor(black);
        table.addCell(cell2);
        //first row  second column
        PdfPCell row1cell2 = new PdfPCell(new Phrase("Rate",font));
        row1cell2.setBorder(Rectangle.NO_BORDER);
        row1cell2.setBackgroundColor(black);
        table.addCell(row1cell2);

        //Date label
        PdfPCell row2cell1 = new PdfPCell(new Phrase("Amount",font));
        row2cell1.setBorder(Rectangle.NO_BORDER);
        row2cell1.setBackgroundColor(black);

        table.addCell(row2cell1);
        //first row  second column

        table.setSpacingBefore(30);
        document.add(table);
    }
    private void combineTwoTables(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell = new PdfPCell();
        cell.setColspan(2);
        cell.setPadding(10);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(addClientTable(document));

        PdfPCell cell2 = new PdfPCell();
        cell2.setPadding(10);
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.addElement(addInvoiceDateDetail());
        table.addCell(cell);
        table.addCell(cell2);
        table.setWidths(new float[]{1,1,2});
        table.setWidthPercentage(100);
        document.add(table);
    }
    private void open_pdf_file(File file) throws IOException, DocumentException {
        openPdfPage(file);
    }

    private Image AddImageInReport() throws IOException, DocumentException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.pdf_logo);
        icon.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
        Image myImg = Image.getInstance(stream.toByteArray());
        myImg.setAlignment(Image.MIDDLE);
        return myImg;
    }

    private Image AddSignatureInReport() throws IOException, DocumentException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap icon = ChangeBase64ToImage(FragmentEditReportUpdate.StBase64ImageToSave);
        cropCenter(icon).compress(Bitmap.CompressFormat.JPEG, 100 , stream);
        Image myImg = Image.getInstance(stream.toByteArray());
        myImg.setScaleToFitHeight(true);
        myImg.setAlignment(Image.ALIGN_TOP);
        return myImg;

    }

    public static Bitmap cropCenter(Bitmap bmp) {
        int dimension = Math.min(bmp.getWidth(), bmp.getHeight());
        return ThumbnailUtils.extractThumbnail(bmp, dimension, dimension-170);
    }

    private void preparePdfDocument(File myFile)
    {
        Document document = new Document(PageSize.A4);
        if (myFile.exists())
            myFile.delete();
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
            document.open();
            addLogoTable(document);
            addBorderTable(document,50,20);
            combineTwoTables(document);
            addHeaderTable(document);
            itemDataTable(document);
            addBorderTable(document,20,20);
            calculatingSubtotal(document);
            addSignatureTable(document);
            document.setMargins(0, 0, 0, 0);
            document.close();
            open_pdf_file(myFile);
        } catch (Exception ex) {
            Log.e("Test321", "Exception = " + ex.getMessage());
        }
    }

    private void addBorderTable(Document document,int before,int after) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setSpacingBefore(before);
        table.setSpacingAfter(after);
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setPadding(10);
        cell.setBorder(Rectangle.BOTTOM);
        table.addCell(cell);

        document.add(table);
    }

    private void addLogoTable(Document document) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell = new PdfPCell(new Phrase("Buisness Name\nBusiness Number: 1234\nLahore\nPakistan\nPunjab\nP: 582828\nM: 8582"));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        PdfPCell cell2 = new PdfPCell();
        cell2.setPadding(10);
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell2);

        PdfPCell cell3 = new PdfPCell();
        cell3.setPadding(10);
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.addElement(AddImageInReport());
        table.addCell(cell3);
        table.setWidths(new float[]{2,0,2});
        document.add(table);
    }
    public void addSignatureTable(Document document) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(3);

        PdfPCell cell1 = new PdfPCell();
        cell1.setPadding(10);
        cell1.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell();
        cell2.setPadding(10);
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell2);
     /*   PdfPCell cell3 = new PdfPCell();
        cell3.setPadding(10);
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.addElement(AddSignatureInReport());
        table.addCell(cell3);*/
        table.setWidths(new float[]{2,0,2});
        document.add(table);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    File myFile=getExternalStorageFile();
                    preparePdfDocument(myFile);
                }else {
                    Toast.makeText(getActivity(), "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
    private void sharePdfInvoice(File file)
    {
        if (file.exists()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("application/pdf");
            getActivity().startActivity(intent);
        } else {
            Log.i("DEBUG", "File doesn't exist");
        }
    }

    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }



    /*Convert Base64 To Image*/

    public Bitmap ChangeBase64ToImage(String str){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(str, Base64.DEFAULT);
        decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
      //  image.setImageBitmap(decodedImage);
        return decodedImage;
    }


    public void showImage(){
        Picasso.get()
                .load(NetworkURLs.BaseURLForImages+FragmentEditReportUpdate.ImagePath)
                .placeholder(R.color.grey) // Your dummy image...
                .into(imageView_Dummy, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        try {
                            create_pdf_file();
                            Toast.makeText(getActivity(), FragmentEditReportUpdate.StBase64ImageToSave, Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                   /*     imageView_Dummy.buildDrawingCache();
                        Bitmap bitmap = imageView_Dummy.getDrawingCache();
                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                        byte[] image=stream.toByteArray();
                        FragmentEditReportUpdate.StBase64ImageToSave= Base64.encodeToString(image, 0);
                        Toast.makeText(getActivity(), FragmentEditReportUpdate.StBase64ImageToSave, Toast.LENGTH_SHORT).show();*/
                       /* imageView_Dummy.buildDrawingCache();
                        Bitmap bitmap = imageView_Dummy.getDrawingCache();
                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
                        byte[] image=stream.toByteArray();

                        //  System.out.println("byte array:"+image);
                        FragmentEditReportUpdate.StBase64ImageToSave= Base64.encodeToString(image, 0);*/
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Unable to load image, may be due to incorrect URL, no network...
                    }


                });
    }

}
