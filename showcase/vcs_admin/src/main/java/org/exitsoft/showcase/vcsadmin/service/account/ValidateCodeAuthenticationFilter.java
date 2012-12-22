package org.exitsoft.showcase.vcsadmin.service.account;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

/**
 * 验证码登录认证Filter
 * 
 * @author vincent
 *
 */
@Component
public class ValidateCodeAuthenticationFilter extends FormAuthenticationFilter{
	
	//默认验证码参数名称
	public static final String DEFAULT_VALIDATE_CODE_PARAM = "validateCode";
	//验证码参数名称
    private String validateCodeParam = DEFAULT_VALIDATE_CODE_PARAM;
    //在session中的存储验证码的key名称
    private String sessionValidateCodeKey = DEFAULT_VALIDATE_CODE_PARAM;
    
    /**
     * 重写父类方法，在shiro执行登录时先对比验证码，正确后在登录，否则直接登录失败
     */
	@Override
	protected boolean executeLogin(ServletRequest request,ServletResponse response) throws Exception {
		
		Session session = getSubject(request, response).getSession(false);
		String code = (String) session.getAttribute(getSessionValidateCodeKey());
		
		if (!StringUtils.equals(code,getValidateCode(request))) {
			return onLoginFailure(this.createToken(request, response), new AccountException("验证码不正确"), request, response);
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

	public String getValidateCode(ServletRequest request) {
		return WebUtils.getCleanParam(request, getValidateCodeParam());
	}
	
	/**
	 * 重写父类方法，当登录失败将异常信息设置到request的attribute中
	 */
	@Override
	protected void setFailureAttribute(ServletRequest request,AuthenticationException ae) {
		request.setAttribute(getFailureKeyAttribute(), ae.getMessage());
	}
}
