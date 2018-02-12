package me.andrewosborn.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@RestController
public class HomeController
{
    @RequestMapping("/")
    public String home()
    {
        getLatestTeamSheetsPDF();

        return "Welcome to CBB Team Sheets";
    }

    private void getLatestTeamSheetsPDF()
    {
        try
        {
            URL url = new URL("https://extra.ncaa.org/solutions/rpi/Stats%20Library/Feb.%2011,%202018%20Team%20Sheets.pdf");
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            PDDocument pdDocument = PDDocument.load(inputStream);
            PDFTextStripper textStripper = new PDFTextStripper();
            String pdfText = textStripper.getText(pdDocument);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
