package com.htmlfinder.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JsoupScanHtmlService implements ScanHtmlService {

    private static final String CHARSET_NAME = "utf8";

    private ElementLookupService elementLookupService;

    public JsoupScanHtmlService(ElementLookupService elementLookupService) {
        this.elementLookupService = elementLookupService;
    }

    @Override
    public List<String> scanElements(String pathToOriginalDocument, String pathToModifiedDocument, String elementId) throws IOException {

        Document originalDocument = getDocument(pathToOriginalDocument);
        Document modifiedDocument = getDocument(pathToModifiedDocument);

        Element specifiedElement = originalDocument.getElementById(elementId);

        List<Element> elements = elementLookupService.findElements(specifiedElement, modifiedDocument);

        return elements.stream()
                       .map(this::getElementsPath)
                       .collect(Collectors.toList());
    }

    public String getElementsPath(Element element) {
        return getElementsPath(element, new StringBuilder());
    }

    private String getElementsPath(Element element, StringBuilder pathBuilder) {
        String tag = element.tagName();

        if (element.hasParent()) {
            Element parent = element.parent();
            Elements siblingElements = element.siblingElements();
            if (containsElementWithTag(siblingElements, tag)) {
                pathBuilder
                        .insert(0, ']')
                        .insert(0, getIndexOfElementWithinElementsWithSameTag(element, siblingElements))
                        .insert(0, '[');
            }
            pathBuilder
                    .insert(0, tag)
                    .insert(0, '/');

            return getElementsPath(parent, pathBuilder);
        } else {
            pathBuilder.insert(0, tag);
            return pathBuilder.toString();
        }

    }

    private boolean containsElementWithTag(Elements elements, String tag) {
        for (Element element : elements) {
            if (tag.equals(element.tagName())) {
                return true;
            }
        }
        return false;
    }

    private int getIndexOfElementWithinElementsWithSameTag(Element element, Elements siblingElements) {
        String tag = element.tagName();

        int countOfElementsWithSameTag = 0;
        for (int i = 0; i < element.elementSiblingIndex(); i++) {
            Element siblingElement = siblingElements.get(0);

            if (tag.equals(siblingElement.tagName())) {
                countOfElementsWithSameTag++;
            }
        }
        return countOfElementsWithSameTag;
    }

    private Document getDocument(String path) throws IOException {
        File file = new File(path);
        return Jsoup.parse(
                file,
                CHARSET_NAME,
                file.getAbsolutePath());
    }
}
