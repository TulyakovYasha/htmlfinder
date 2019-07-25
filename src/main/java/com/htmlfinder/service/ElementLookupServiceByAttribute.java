package com.htmlfinder.service;

import com.htmlfinder.utils.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ElementLookupServiceByAttribute implements ElementLookupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElementLookupServiceByAttribute.class);

    @Override
    public List<Element> findElements(Element element, Document document) {
        LOGGER.info("Looking for element {}", element);
        Attributes elementAttributes = element.attributes();
        String tagName = element.tagName();

        List<Attribute> attributePriorities = Arrays.stream(Attribute.values())
                                                    .filter(attribute -> elementAttributes.hasKeyIgnoreCase(attribute.getName()))
                                                    .sorted(Comparator.comparingInt(Attribute::getPriority))
                                                    .collect(Collectors.toList());

        Elements elementsByTag = document.getElementsByTag(tagName);
        LOGGER.debug("Found {} elements by tag {}", elementsByTag.size(), tagName);

        return findMatchingElementsByAttributes(attributePriorities, elementAttributes, elementsByTag);
    }

    private List<Element> findMatchingElementsByAttributes(List<Attribute> attributes, Attributes elementAttributes, Elements elements) {
        Elements matchingElements = elements;

        for (Attribute attribute : attributes) {
            String attributeName = attribute.getName();
            String attributeValue = elementAttributes.get(attributeName);

            if (matchingElements.hasAttr(attributeName)) {
                Elements filteredElements = matchingElements
                        .stream()
                        .filter(element -> attributeValue.equals(element.attr(attributeName)))
                        .collect(Collectors.toCollection(Elements::new));
                if (!filteredElements.isEmpty()) {
                    matchingElements = filteredElements;
                }
            }
        }
        return matchingElements != elements ? matchingElements : Collections.emptyList();
    }

}
