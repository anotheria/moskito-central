package org.moskito.central.storage.serializer;

import org.moskito.central.Snapshot;
import org.moskito.central.storage.SnapshotSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * @author askrypnyk
 * @since 14.04.15 14:00
 */
public class XMLSerializer implements SnapshotSerializer {
    private static Logger log = LoggerFactory.getLogger(XMLSerializer.class);


    @Override
    public byte[] serialize(Snapshot snapshot) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            createDOM(snapshot, outputStream);
        } catch (Exception e) {
            log.warn("Snapshot " + snapshot + " couldn't be serialized", e);
        }

        return outputStream.toByteArray();
    }

    private void createDOM(Snapshot snapshot, ByteArrayOutputStream outputStream) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();

//        snapshot root element
        Element rootElement = document.createElement("snapshot");
        document.appendChild(rootElement);

//        metadata element
        Element metadata = document.createElement("metadata");
        rootElement.appendChild(metadata);

        if (snapshot.getMetaData().getProducerId() != null) {
            Element producerId = document.createElement("producerId");
            producerId.appendChild(document.createTextNode(snapshot.getMetaData().getProducerId()));
            metadata.appendChild(producerId);
        }

        if (snapshot.getMetaData().getHostName() != null) {

            Element hostName = document.createElement("hostName");
            hostName.appendChild(document.createTextNode(snapshot.getMetaData().getHostName()));
            metadata.appendChild(hostName);
        }

        if (snapshot.getMetaData().getCreationTimestamp() != 0) {
            Element creationTimestamp = document.createElement("creationTimestamp");
            creationTimestamp.appendChild(document.createTextNode(Long.toString(snapshot.getMetaData().getCreationTimestamp())));
            metadata.appendChild(creationTimestamp);
        }

        if (snapshot.getMetaData().getArrivalTimestamp() != 0) {
            Element arrivalTimestamp = document.createElement("arrivalTimestamp");
            arrivalTimestamp.appendChild(document.createTextNode(Long.toString(snapshot.getMetaData().getArrivalTimestamp())));
            metadata.appendChild(arrivalTimestamp);
        }

        if (snapshot.getMetaData().getCategory() != null) {
            Element category = document.createElement("category");
            category.appendChild(document.createTextNode(snapshot.getMetaData().getCategory()));
            metadata.appendChild(category);
        }

        if (snapshot.getMetaData().getSubsystem() != null) {
            Element subsystem = document.createElement("subsystem");
            subsystem.appendChild(document.createTextNode(snapshot.getMetaData().getSubsystem()));
            metadata.appendChild(subsystem);
        }

        if (snapshot.getMetaData().getStatClassName() != null) {
            Element statClassName = document.createElement("statClassName");
            statClassName.appendChild(document.createTextNode(snapshot.getMetaData().getStatClassName()));
            metadata.appendChild(statClassName);
        }

//        statistic elements
        Element stats = document.createElement("stats");
        rootElement.appendChild(stats);

        for (Map.Entry<String, Map<String, String>> entry : snapshot.getStats().entrySet()) {
            Element statsNode = document.createElement(entry.getKey());
            stats.appendChild(statsNode);

            for (Map.Entry<String, String> stringEntry : entry.getValue().entrySet()) {
                Element node = document.createElement(stringEntry.getKey());
                node.appendChild(document.createTextNode(stringEntry.getValue()));
                statsNode.appendChild(node);
            }

        }

//        build xml readable view
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(outputStream);

        transformer.transform(source, result);

    }


}