package org.exitsoft.showcase.service.account;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

/**
 * 验证码登录认证Filter
 * 
 * @author vincent
 *
 */
@Component
public class ValidateCodeAuthenticationFilter extends FormAuthenticationFilter{
	
	/**
	 * 默认验证码参数名称
	 */
	public static final String DEFAULT_VALIDATE_CODE_PARAM = "validateCode";
	/**
	 * 默认在session中存储的登录次数名称
	 */
	private static final String DEFAULT_LOGIN_NUM_KEY = "loginNum";
	//验证码参数名称
    private String validateCodeParam = DEFAULT_VALIDATE_CODE_PARAM;
    //在session中的存储验证码的key名称
    private String sessionValidateCodeKey = DEFAULT_VALIDATE_CODE_PARAM;
    //在session中存储的登录次数名称
    private String loginNumKey = DEFAULT_LOGIN_NUM_KEY;
    //允许登录次数，当登录次数大于该数值时，会在页面中显示验证码
    private Integer allowLoginNum = 1;
    
    /**
     * 重写父类方法，在shiro执行登录时先对比验证码，正确后在登录，否则直接登录失败
     */
	@Override
	protected boolean executeLogin(ServletRequest request,ServletResponse response) throws Exception {
		
		Session session = getSubject(request, response).getSession();
		//获取登录次数
		Integer number = (Integer) session.getAttribute(getLoginNumKey());
		
		//首次登录，将该数量记录在session中
		if (number == null) {
			number = new Integer(1);
			session.setAttribute(getLoginNumKey(), number);
		}
		
		//如果登录次数大于1，需要判断验证码是否一致
		if (number > getAllowLoginNum()) {
			//获取当前验证码
			String code = (String) session.getAttribute(getSessionValidateCodeKey());
			//获取用户输入的验证码
			String submitCode = getValidateCode(request);
			//如果验证码不匹配，登录失败
			if (StringUtils.isEmpty(submitCode) || !StringUtils.equals(code,submitCode.toLowerCase())) {
				return onLoginFailure(this.createToken(request, response), new AccountException("验证码不正确"), request, response);
			}
		
		}
		
		return super.executeLogin(request, response);
	}

	/**
	 * 设置验证码提交的参数名称
	 * 
	 * @param validateCodeParam 验证码提交的参数名称
	 */
	public void setValidateCodeParam(String validateCodeParam) {
		this.validateCodeParam = validateCodeParam;
	}

	/**
	 * 获取验证码提交的参数名称
	 * 
	 * @return String
	 */
	public String getValidateCodeParam() {
		return validateCodeParam;
	}

	/**
	 * 设置在session中的存储验证码的key名称
	 * 
	 * @param sessionValidateCodeKey 存储验证码的key名称
	 */
	public void setSessionValidateCodeKey(String sessionValidateCodeKey) {
		this.sessionValidateCodeKey = sessionValidateCodeKey;
	}
	
	/**
	 * 获取设置在session中的存储验证码的key名称
	 * 
	 * @return Sting
	 */
	public String getSessionValidateCodeKey() {
		return sessionValidateCodeKey;
	}

	/**
	 * 获取在session中存储的登录次数名称
	 * 
	 * @return Stromg
	 */
	public String getLoginNumKey() {
		return loginNumKey;
	}

	/**
	 * 设置在session中存储的登录次数名称
	 * 
	 * @param loginNumKey 登录次数名称
	 */
	public void setLoginNumKey(String loginNumKey) {
		this.loginNumKey = loginNumKey;
	}

	/**
	 * 获取用户输入的验证码
	 * 
	 * @param request ServletRequest
	 * 
	 * @return String
	 */
	public String getValidateCode(ServletRequest request) {
		return WebUtils.getCleanParam(request, getValidateCodeParam());
	}
	
	/**
	 * 获取允许登录次数
	 * 
	 * @return Integer
	 */
	public Integer getAllowLoginNum() {
		return allowLoginNum;
	}

	/**
	 * 设置允许登录次数，当登录次数大于该数值时，会在页面中显示验证码
	 * 
	 * @param allowLoginNum 允许登录次数
	 */
	public void setAllowLoginNum(Integer allowLoginNum) {
		this.allowLoginNum = allowLoginNum;
	}

	/**
	 * 重写父类方法，当登录失败将异常信息设置到request的attribute中
	 */
	@Override
	protected void setFailureAttribute(ServletRequest request,AuthenticationException ae) {
		if (ae instanceof IncorrectCredentialsException) {
			request.setAttribute(getFailureKeyAttribute(), "用户名密码不正确");
		} else {
			request.setAttribute(getFailureKeyAttribute(), ae.getMessage());
		}
	}
	
	/**
	 * 重写父类方法，当登录失败次数大于allowLoginNum（允许登录次）时，将显示验证码
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,AuthenticationException e, ServletRequest request,ServletResponse response) {
		Session session = getSubject(request, response).getSession(false);
		
		Integer number = (Integer) session.getAttribute(getLoginNumKey());
		
		if (number > getAllowLoginNum() - 1) {
			request.setAttribute(getValidateCodeParam(),true);
		}
		
		return super.onLoginFailure(token, e, request, response);
	}
	
	/**
	 * 重写父类方法，当登录成功后，将allowLoginNum（允许登录次）设置为0，重置下一次登录的状态
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		subject.getSession(false).setAttribute(getLoginNumKey(), null);
		return super.onLoginSuccess(token, subject, request, response);
	}
	
	/**
	 * 重写父类方法，创建一个自定义的{@link UsernamePasswordTokeExtend}
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		
		String username = getUsername(request);
	    String password = getPassword(request);
        String host = getHost(request);

	    boolean rememberMe = false;
	    String rememberMeValue = request.getParameter(getRememberMeParam());
	    Integer rememberMeCookieValue = null;
	    //如果提交的rememberMe参数存在值,将rememberMe设置成true
	    if(StringUtils.isNotEmpty(rememberMeValue)) {
	    	rememberMe = true;
	    	
	    	ExpressionParser parser = new SpelExpressionParser();
			Expression expression = parser.parseExpression(rememberMeValue);
	    	
	    	rememberMeCookieValue = expression.getValue(Integer.class);
	    }
	    
		return new UsernamePasswordTokeExtend(username, password, rememberMe, host,rememberMeCookieValue);
	}
	
	/**
	 * UsernamePasswordToke扩展，添加一个rememberMeValue字段，获取提交上来的rememberMe值
	 * 根据该rememberMe值去设置Cookie的有效时间。
	 * 
	 * @author vincent
	 *
	 */
	@SuppressWarnings("serial")
	protected class UsernamePasswordTokeExtend extends UsernamePasswordToken {
		
		//rememberMe cookie的有效时间
		private Integer rememberMeCookieValue;
		
		public UsernamePasswordTokeExtend() {
			
		}
		
		public UsernamePasswordTokeExtend(String username,String password,boolean rememberMe, String host,Integer rememberMeCookieValue) {
			super(username, password, rememberMe, host);
			this.rememberMeCookieValue = rememberMeCookieValue;
		}

		/**
		 * 获取rememberMe cookie的有效时间
		 * 
		 * @return Integer
		 */
		public Integer getRememberMeCookieValue() {
			return rememberMeCookieValue;
		}

		/**
		 * 设置rememberMe cookie的有效时间
		 * 
		 * @param rememberMeCookieValue cookie的有效时间
		 */
		public void setRememberMeCookieValue(Integer rememberMeCookieValue) {
			this.rememberMeCookieValue = rememberMeCookieValue;
		}
		
		
	}
}
