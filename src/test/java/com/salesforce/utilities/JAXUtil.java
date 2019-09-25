package com.salesforce.utilities;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXUtil {
	private JAXUtil() {
		// static methods only
	}

	/**
	 * @param <T>
	 *            the type we want to convert the XML into
	 * @param entityClass
	 *            the class of the parameterized type
	 * @param xml
	 *            the instance XML description
	 * @return a deserialization of the XML into an object of type T of class
	 *         class <T>
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T marshal(Class<T> entityClass, String xml) throws JAXBException {
		T res;
		if (entityClass == xml.getClass()) {
			res = (T) xml;
		} else {
			JAXBContext ctx = JAXBContext.newInstance(entityClass);
			Unmarshaller marshaller = ctx.createUnmarshaller();
			res = (T) marshaller.unmarshal(new StringReader(xml));
		}
		return res;
	}

	/**
	 * @param <T>
	 *            the type to serialize
	 * @param c
	 *            the class of the type to serialize
	 * @param entity
	 *            the instance containing the data to serialize
	 * @return a string representation of the data.
	 * @throws Exception
	 */
	public static <T> String unmarshal(T entity) throws Exception {
		JAXBContext ctx = JAXBContext.newInstance(entity.getClass());
		Marshaller marshaller = ctx.createMarshaller();
		StringWriter entityXml = new StringWriter();
		marshaller.marshal(entity, entityXml);
		String entityString = entityXml.toString();
		return entityString;
	}

}
