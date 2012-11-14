package org.exitsoft.common.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;

/**
 * JAXB工具类
 * 
 * @author vincent
 *
 */
public class JaxbUtils {
	
	//默认xml编码
	private static final String DEFAULT_ENCODING = "UTF-8"; 

	/**
	 * 将Java参数对象转换为xml，默认encoding为UTF-8 &lt;?xml version="1.0" encoding="UTF-8"?&gt;
	 * 
	 * @param root Java对象
	 * 
	 * @return String
	 */
	public static String marshaller(Object root) {
		return marshaller(root, null);
	}

	/**
	 * 将Java参数对象转换为xml
	 * 
	 * @param root Java对象
	 * @param encoding xml编码
	 * 
	 * @return String
	 */
	public static String marshaller(Object root, String encoding) {
		return marshaller(root, ReflectionUtils.getTargetClass(root), encoding);
	}

	/**
	 * 将Java对象转换为xml
	 * 
	 * @param root Java对象
	 * @param rootClass 对象Class
	 * @param encoding xml编码
	 * 
	 * @reutrn String
	 */
	public static String marshaller(Object root, Class<?> targetClass, String encoding) {
		try {
			StringWriter writer = new StringWriter();
			createMarshaller(targetClass, encoding).marshal(root, writer);
			return writer.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 将Collection接口子类转换为xml对象
	 * 
	 * @param root
	 * @param rootName
	 * @param clazz
	 * 
	 * @return String
	 * 
	 */
	public static String marshaller(Collection<?> root, String rootName, Class<?> targetClass) {
		return marshaller(root, rootName, targetClass, null);
	}

	/**
	 * 将Java集合Collection类型的对象转换为Xml
	 * 
	 * @param root Java集合
	 * @param rootName 节点名称
	 * @param clazz 类型Class
	 * @param encoding xml编码
	 * 
	 * @return String
	 */
	public static String marshaller(Collection<?> root, String rootName, Class<?> targetClass, String encoding) {
		try {
			CollectionWrapper wrapper = new CollectionWrapper();
			wrapper.collection = root;

			JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName),CollectionWrapper.class, wrapper);

			StringWriter writer = new StringWriter();
			createMarshaller(targetClass, encoding).marshal(wrapperElement, writer);

			return writer.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 将Xml转换为Java对象
	 * 
	 * @param xml xml字符串
	 * @param destinationClass 对象Class
	 * 
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(String xml, Class<T> targetClass) {
		try {
			StringReader reader = new StringReader(xml);
			return (T) createUnmarshaller(targetClass).unmarshal(reader);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 创建Marshaller对象
	 * 
	 * @param clazz
	 * @param encoding
	 * 
	 * @return {@link Marshaller}
	 */
	public static Marshaller createMarshaller(Class<?> targetClass, String encoding) {
		try {
			
			JAXBContext jaxbContext = createJAXBContext(targetClass);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, !StringUtils.isNotBlank(encoding) ? DEFAULT_ENCODING : encoding);
			
			return marshaller;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 创建UnMarshaller对象
	 * 
	 * @param clazz
	 * 
	 * @return {@link Unmarshaller}
	 */
	public static Unmarshaller createUnmarshaller(Class<?> targetClass) {
		try {
			JAXBContext jaxbContext = createJAXBContext(targetClass);
			return jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	protected static JAXBContext createJAXBContext(Class<?> classesToBeBound) {
		try {
			return JAXBContext.newInstance(classesToBeBound, CollectionWrapper.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 封装Root Element 是 Collection的情况.
	 */
	public static class CollectionWrapper {

		@XmlAnyElement
		protected Collection<?> collection;
	}
}
