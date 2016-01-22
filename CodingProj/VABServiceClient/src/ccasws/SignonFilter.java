package ccasws;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpSession;


public class SignonFilter implements Filter
{
	//String LOGIN_PAGE="/clpay/user/login.jsp";
	protected FilterConfig filterConfig;

	//过滤处理的方法
	public void doFilter(final ServletRequest req,final ServletResponse res,FilterChain chain)throws IOException,ServletException
	{
			HttpServletRequest hreq = (HttpServletRequest)req;
			HttpServletResponse hres = (HttpServletResponse)res;
			String currentpage=hreq.getRequestURL().toString();
			HttpSession session = hreq.getSession();
			String isLogin="";
			String nexturl="";
			try
			{
				isLogin=(String)session.getAttribute("isLogin");
				if	(isLogin==null)	isLogin="false";
				if	(isLogin.equalsIgnoreCase("true"))
				{
					//CLog.writeLog((String)session.getAttribute("operLevel")+"在SignonFilter中验证通过"+currentpage);
					//ObData obData=new ObData();
					//if	(!obData.checkAuthority((String)session.getAttribute("operLevel"),currentpage)){
						//nexturl=(String)session.getAttribute("nexturl");
						//if	(nexturl==null || nexturl.length()==0)	nexturl="/MainMenu.jsp";
						//hres.sendRedirect("/NoAuthor.jsp");
						//CLog.writeLog((String)session.getAttribute("operLevel")+"被SignonFilter拦截一个未授权的请求"+currentpage);
					//}else{
						//session.setAttribute("nexturl",currentpage);
						//CLog.writeLog((String)session.getAttribute("operLevel")+"在SignonFilter中授权通过"+currentpage);
					//}

					//验证成功，继续处理
					chain.doFilter(req,res);

				}
				else
				{
					CLog.writeLog("被SignonFilter拦截一个未认证的请求"+currentpage);
					nexturl=(String)session.getAttribute("nexturl");
					if	(nexturl==null || nexturl.length()==0)
					{
						session.setAttribute("nexturl",currentpage);
						nexturl=currentpage;
					}
					session.setAttribute("nexturl",nexturl);
					CLog.writeLog("javabean.nexturl=["+nexturl+"]");
					//验证不成功，让用户登录。
					//hres.sendRedirect(LOGIN_PAGE);
					chain.doFilter(req,res);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

	}

	public void setFilterConfig(final FilterConfig filterConfig)
	{
		this.filterConfig=filterConfig;
	}

	//销毁过滤器
	public void destroy()
	{
		this.filterConfig=null;
	}
		/**
			*初始化过滤器,和一般的Servlet一样，它也可以获得初始参数。
		*/
		public void init(FilterConfig config) throws ServletException {
			this.filterConfig = config;
		}

}
