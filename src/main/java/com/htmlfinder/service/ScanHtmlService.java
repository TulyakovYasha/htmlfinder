package com.htmlfinder.service;

import java.io.IOException;
import java.util.List;

public interface ScanHtmlService {

    List<String> scanElements(String originalDocumentPath, String modifiedDocumentPath, String elementId)
            throws IOException;
}
