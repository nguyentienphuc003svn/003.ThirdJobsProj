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

	//���˴���ķ���
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
					//CLog.writeLog((String)session.getAttribute("operLevel")+"��SignonFilter����֤ͨ��"+currentpage);
					//ObData obData=new ObData();
					//if	(!obData.checkAuthority((String)session.getAttribute("operLevel"),currentpage)){
						//nexturl=(String)session.getAttribute("nexturl");
						//if	(nexturl==null || nexturl.length()==0)	nexturl="/MainMenu.jsp";
						//hres.sendRedirect("/NoAuthor.jsp");
						//CLog.writeLog((String)session.getAttribute("operLevel")+"��SignonFilter����һ��δ��Ȩ������"+currentpage);
					//}else{
						//session.setAttribute("nexturl",currentpage);
						//CLog.writeLog((String)session.getAttribute("operLevel")+"��SignonFilter����Ȩͨ��"+currentpage);
					//}

					//��֤�ɹ�����������
					chain.doFilter(req,res);

				}
				else
				{
					CLog.writeLog("��SignonFilter����һ��δ��֤������"+currentpage);
					nexturl=(String)session.getAttribute("nexturl");
					if	(nexturl==null || nexturl.length()==0)
					{
						session.setAttribute("nexturl",currentpage);
						nexturl=currentpage;
					}
					session.setAttribute("nexturl",nexturl);
					CLog.writeLog("javabean.nexturl=["+nexturl+"]");
					//��֤���ɹ������û���¼��
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

	//���ٹ�����
	public void destroy()
	{
		this.filterConfig=null;
	}
		/**
			*��ʼ��������,��һ���Servletһ������Ҳ���Ի�ó�ʼ������
		*/
		public void init(FilterConfig config) throws ServletException {
			this.filterConfig = config;
		}

}
