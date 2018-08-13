package com.rank.search.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 对Jsoup的再次封装,更加简明扼要
 * 
 * @author zel
 * 
 */
public class JsoupHtmlParser {

	/**
	 * 对选择器的默认调用
	 * 
	 * @param htmlSource
	 * @param selectorList
	 * @param isFilter
	 * @return
	 */
	public static List<String> getNodeContentBySelector(String htmlSource,
			List<String> selectorList, boolean isFilter) {
		return getNodeContentBySelector(htmlSource, selectorList,
				DataFormatStatus.CleanTxt, isFilter);
	}

	/**
	 * 通过选择器来检索和获取节点数据,直接以集合形式传参
	 *
	 * @param htmlSource
	 * @param selectorList
	 * @return
	 */
	public static List<String> getNodeContentBySelector(String htmlSource,
			List<String> selectorList, DataFormatStatus dataFormatStatus,
			boolean isFilter) {
		if (htmlSource == null || htmlSource.isEmpty() || selectorList == null
				|| selectorList.isEmpty()) {
			return null;
		}
		SystemAssert.assertNotNull(dataFormatStatus);

		Document doc = Jsoup.parse(htmlSource);// 先预解析

		Iterator<String> selectorIteraotr = selectorList.iterator();
		String temp_selector = null;
		List<Element> temp_list_element = new LinkedList<Element>();
		Elements elements = null;
		Elements temp_elements = null;
		// 暂存循环迭代时候的结果
		List<String> temp_list_line = new LinkedList<String>();
		Document temp_doc = null;// 暂存二次解析出来的doc
		boolean isFirst = true;// 标志是否是第一次进行选择器处理
		while (selectorIteraotr.hasNext()) {

			temp_selector = selectorIteraotr.next();
			if (isFirst) {
				elements = doc.select(temp_selector);

				//System.out.println("elements: "+elements.toString());
				isFirst = false;
			} else {
				/**
				 * 对每个先前的结果集进行过滤
				 */
				elements.clear();
				for (String line : temp_list_line) {
					if (line != null && (!line.isEmpty())) {
						temp_doc = Jsoup.parse(line);// 先预解析
						temp_elements = temp_doc.select(temp_selector);

						if (temp_elements != null && (!temp_elements.isEmpty())) {
							elements.addAll(temp_elements);
						}
					}
				}
			}
			temp_list_element.clear();
			temp_list_element.addAll(elements);

			Iterator<Element> elementIteraotr = temp_list_element.iterator();

			temp_list_line.clear();
			while (elementIteraotr.hasNext()) {
				Element element = elementIteraotr.next();
                //System.out.println("element: "+element);

				temp_list_line.add(element.toString());
			}
		}
		// 做下过滤
		temp_list_line = doListFilter(temp_list_line, dataFormatStatus,
				isFilter);


		return temp_list_line;
	}

	/**
	 * 做最后的字符串过滤，该方法对用户透明
	 *
	 * @param temp_list_line
	 * @param dataFormatStatus
	 * @return
	 */
	private static List<String> doListFilter(List<String> temp_list_line,
											 DataFormatStatus dataFormatStatus, boolean isFilter) {
		if (temp_list_line == null || temp_list_line.isEmpty()) {
			return null;
		}

		SystemAssert.assertNotNull(dataFormatStatus);

		// 最终的结合集进行所要的数据格式的过滤
		if (dataFormatStatus == DataFormatStatus.CleanTxt) {
			List<String> cleanResultList = new LinkedList<String>();
			String temp_clean = null;
			for (String item : temp_list_line) {
				if (isFilter) {
					item = item.replaceAll("", "");
				}
				if ((temp_clean = getCleanTxt(item)) != null
						&& (!temp_clean.isEmpty())) {
					cleanResultList.add(temp_clean);
				}
			}
			return cleanResultList;
		}
		return temp_list_line;
	}

	/**
	 * 得到指定文档的纯文档
	 *
	 * @param htmlSource
	 * @return
	 */
	public static String getCleanTxt(String htmlSource) {
		if (htmlSource == null || htmlSource.isEmpty()) {
			return null;
		}
		return Jsoup.clean(htmlSource, Whitelist.none());
	}

	/**
	 * 去掉标签中某一部分内容,暂定位第一版
	 *
	 * @param htmlSource
	 * @param selector
	 * @param removeSelector
	 * @return
	 */
	public static String removeInnerContent(String htmlSource, String selector,
											List<String> removeSelector) {
		if (selector == null
				|| StringOperatorUtil.isBlankCollection(removeSelector)) {
			return htmlSource;
		}
		try {
			Document doc = Jsoup.parse(htmlSource);// 先预解析
			Elements elements = doc.select(selector);
			String result = null;
			if (elements != null) {
				for (Element ele : elements) {
					for (String sel : removeSelector) {
						ele.select(sel).remove();
					}
					// result = ele.toString();
					//result = JsoupHtmlParser.getCleanTxt(ele.toString());
					//break;
				}
			}
			// 转string
			return doc.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}