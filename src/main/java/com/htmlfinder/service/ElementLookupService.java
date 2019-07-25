package com.htmlfinder.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public interface ElementLookupService {
    List<Element> findElements(Element element, Document document);
}
