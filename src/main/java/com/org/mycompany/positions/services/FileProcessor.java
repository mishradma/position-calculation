/**
 * 
 */
package com.org.mycompany.positions.services;

import java.util.List;

import org.springframework.messaging.Message;

import com.google.common.collect.Lists;

/**
 * @author Dayanand Mishra
 *
 */
public interface FileProcessor {
	final String HEADER_FILE_NAME = "file_name";

	/**
	 * Default funmction
	 * 
	 * @param msg
	 * @since JDK1.8
	 */
	public default void process(Message<String> msg) {
		final String MSG = "%s received. Content: %s";
		String fileName = (String) msg.getHeaders().get(HEADER_FILE_NAME);
		String content = msg.getPayload();
		System.out.println(String.format(MSG, fileName, content));

	};

	default List<String> extractLines(Message<String> msg) {
		String content = msg.getPayload();
		List<String> contentList = Lists.newArrayList(content.split("\r\n"));
		return contentList;
	}

}
