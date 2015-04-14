package org.moskito.central.storage.serializer;

import org.moskito.central.Snapshot;
import org.moskito.central.storage.SnapshotSerializer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;

/**
 * @author askrypnyk
 * @since 14.04.15 14:00
 */
public class XMLSerializer implements SnapshotSerializer {


    @Override
    public byte[] serialize(Snapshot snapshot) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            JAXBContext context = JAXBContext.newInstance(Snapshot.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(snapshot, outputStream);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}