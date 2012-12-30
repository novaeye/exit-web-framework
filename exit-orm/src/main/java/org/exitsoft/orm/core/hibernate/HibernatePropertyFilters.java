package org.exitsoft.orm.core.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.exitsoft.common.utils.ServletUtils;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyFilterBuilder;
import org.hibernate.criterion.Criterion;

public abstract class HibernatePropertyFilters {
	
	private static HibernateRestrictionBuilder builder = new HibernateRestrictionBuilder();
	
	public static List<PropertyFilter> createPropertyFilters(String[] expressions, String[] matchValues) {
		return builder.createPropertyFilters(expressions, matchValues);
	}
	
	
	public static Criterion getRestriction(PropertyFilter filter) {
		return builder.getRestriction(filter);
	}

	
	public static Criterion getRestriction(String propertyName, Object value,String restrictionName) {
		return builder.getRestriction(propertyName, value,restrictionName);
	}
	
	/**
	 * 通过表达式和对比值创建属性过滤器
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	PropertyFilerRestriction.createrPropertyFilter("EQS_propertyName","vincent")
	 * </code>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * 
	 * @return {@link PropertyFilter}
	 */
	public static PropertyFilter createPropertyFilter(String expression, String matchValue) {
		return builder.createPropertyFilter(expression, matchValue);
	}
	
	/**
	 * 从HttpRequest参数中创建PropertyFilter列表, 默认Filter属性名前缀为filter.
	 * 当参数存在{filter_EQS_property1:value,filter_EQS_property2:''}该形式的时候，将不会创建filter_EQS_property2等于""值的实例
	 * 参考{@link PropertyFilterBuilder#buildPropertyFilter(HttpServletRequest, String, boolean)}
	 * 
	 * @param request HttpServletRequest
	 */
	public static List<PropertyFilter> buildFromHttpRequest(HttpServletRequest request) {
		return buildFromHttpRequest(request, "filter");
	}
	
	/**
	 * 从HttpRequest参数中创建PropertyFilter列表,当参数存在{filter_EQS_property1:value,filter_EQS_property2:''}
	 * 该形式的时候，将不会创建filter_EQS_property2等于""值的实例
	 * 参考{@link PropertyFilterBuilder#buildPropertyFilter(HttpServletRequest, String, boolean)}
	 * 
	 * @param request HttpServletRequest
	 * @param filterPrefix 用于识别是propertyfilter参数的前准
	 * 
	 * @return List
	 */
	public static List<PropertyFilter> buildFromHttpRequest(HttpServletRequest request,String filterPrefix) {
		return buildPropertyFilter(request, "filter",false);
	}
	
	/**
	 * 从HttpRequest参数中创建PropertyFilter列表,当参数存在{filter_EQS_property1:value,filter_EQS_property2:''}
	 * 该形式的时候，将不会创建filter_EQS_property2等于""值的实例
	 * 参考{@link PropertyFilterBuilder#buildPropertyFilter(HttpServletRequest, String, boolean)}
	 * 
	 * <pre>
	 * 当页面提交的参数为:{filter_EQS_property1:value,filter_EQS_property2:''}
	 * List filters =buildPropertyFilter(request,"filter",false);
	 * 当前filters:EQS_proerpty1="value",EQS_proerpty1=""
	 * 
	 * 当页面提交的参数为:{filter_EQS_property1:value,filter_EQS_property2:''}
	 * List filters =buildPropertyFilter(request,"filter",true);
	 * 当前filters:EQS_proerpty1="value"
	 * </pre>
	 * 
	 * @param request HttpServletRequest
	 * @param ignoreEmptyValue true表示当存在""值时忽略该PropertyFilter
	 * 
	 * @return List
	 */
	public static List<PropertyFilter> buildFromHttpRequest(HttpServletRequest request,boolean ignoreEmptyValue) {
		return buildPropertyFilter(request, "filter",ignoreEmptyValue);
	}

	/**
	 * 从HttpRequest参数中创建PropertyFilter列表,例子:
	 * 
	 * <pre>
	 * 当页面提交的参数为:{filter_EQS_property1:value,filter_EQS_property2:''}
	 * List filters =buildPropertyFilter(request,"filter",false);
	 * 当前filters:EQS_proerpty1="value",EQS_proerpty1=""
	 * 
	 * 当页面提交的参数为:{filter_EQS_property1:value,filter_EQS_property2:''}
	 * List filters =buildPropertyFilter(request,"filter",true);
	 * 当前filters:EQS_proerpty1="value"
	 * </pre>
	 * 
	 * @param request HttpServletRequest
	 * @param filterPrefix 用于识别是propertyfilter参数的前准
	 * @param ignoreEmptyValue true表示当存在""值时忽略该PropertyFilter
	 * 
	 * @return List
	 */
	public static List<PropertyFilter> buildPropertyFilter(HttpServletRequest request,String filterPrefix,boolean ignoreEmptyValue) {

		// 从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
		Map<String, Object> filterParamMap = ServletUtils.getParametersStartingWith(request, filterPrefix + "_");

		return buildPropertyFilter(filterParamMap,ignoreEmptyValue);
	}
	
	/**
	 * 从Map中创建PropertyFilter列表，如:
	 * 
	 * <pre>
     * Map o = new HashMap();
	 * o.put("EQS_property1","value");
	 * o.put("EQS_property2","");
	 * List filters = buildPropertyFilter(o);
	 * 当前filters:EQS_proerpty1="value",EQS_proerpty1=""
     * </pre>
	 * 
	 * 
	 * @param filters 过滤器信息
	 * 
	 */
	public static List<PropertyFilter> buildPropertyFilter(Map<String, Object> filters) {
		
		return buildPropertyFilter(filters,false);
	}
	
	/**
	 * 从Map中创建PropertyFilter列表，如:
	 * 
	 * <pre>
     * Map o = new HashMap();
	 * o.put("EQS_property1","value");
	 * o.put("EQS_property2","");
	 * List filters = buildPropertyFilter(o,false);
	 * 当前filters:EQS_proerpty1="value",EQS_proerpty1=""
	 * 
	 * Map o = new HashMap();
	 * o.put("EQS_property1","value");
	 * o.put("EQS_property2","");
	 * List filters = buildPropertyFilter(o,true);
	 * 当前filters:EQS_proerpty1="value"
     * </pre>
	 * 
	 * 
	 * @param filters 过滤器信息
	 * @param ignoreEmptyValue true表示当存在 null或者""值时忽略该PropertyFilter
	 * 
	 */
	public static List<PropertyFilter> buildPropertyFilter(Map<String, Object> filters,boolean ignoreEmptyValue) {
		List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();
		// 分析参数Map,构造PropertyFilter列表
		for (Map.Entry<String, Object> entry : filters.entrySet()) {
			String expression = entry.getKey();
			Object value = entry.getValue();
			//如果ignoreEmptyValue为true忽略null或""的值
			if (ignoreEmptyValue && (value == null || value.toString().equals(""))) {
				continue;
			}
			//如果ignoreEmptyValue为true忽略null或""的值
			PropertyFilter filter = createPropertyFilter(expression, value.toString());
			filterList.add(filter);
			
		}
		return filterList;
	}
}
