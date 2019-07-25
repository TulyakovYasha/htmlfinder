package com.htmlfinder;

import com.htmlfinder.service.ElementLookupServiceByAttribute;
import com.htmlfinder.service.ElementLookupService;
import com.htmlfinder.service.ScanHtmlService;
import com.htmlfinder.service.JsoupScanHtmlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {

    private static final String ELEMENT_ID = "make-everything-ok-button";
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ElementLookupService elementLookupService;
        ScanHtmlService scanHtmlService;

        if (args == null || args.length < 2) {
            LOGGER.error("Incorrect arguments [{}]", args);
            return;
        }
        String originalFilePath = args[0];
        String modifiedFilePath = args[1];

        elementLookupService = new ElementLookupServiceByAttribute();
        scanHtmlService = new JsoupScanHtmlService(elementLookupService);

        try {
            List<String> elements = scanHtmlService.scanElements(originalFilePath, modifiedFilePath, ELEMENT_ID);
            if (elements.isEmpty()) {
                System.out.println("Necessary element is not found");
            } else {
                System.out.println(String.format("There is %d elements found:", elements.size()));
                for (String element : elements) {
                    System.out.println("\t" + element);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error during scanning html files", e);
            System.out.println("Error during scanning html files");
        }
    }
}
