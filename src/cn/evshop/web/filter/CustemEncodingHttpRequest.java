package cn.evshop.web.filter;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.sun.xml.internal.ws.encoding.HasEncoding;


public class CustemEncodingHttpRequest extends HttpServletRequestWrapper {

	private HttpServletRequest request;
	private boolean haseEncode;
	
	public CustemEncodingHttpRequest(HttpServletRequest request) {
		super(request);
		this.request = request; //必须
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Map getParameterMap()
	{
		//先获取请求方法
		String method = request.getMethod();
		if(method.equalsIgnoreCase("post"))
		{
			try {
				//处理post乱码
				request.setCharacterEncoding("utf-8");
				return request.getParameterMap();
			} catch (UnsupportedEncodingException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}else if(method.equalsIgnoreCase("get"))
		{
			Map<String, String[]> parameterMap = request.getParameterMap();
			if(!haseEncode) //确保get手动编码逻辑只运行一次
			{
				for(String paramName : parameterMap.keySet())
				{
					String[] values = parameterMap.get(paramName);
					if(values != null)
					{
						
						for(int i = 0;i < values.length; i++)
						{
							try {
								values[i] = new String(values[i].getBytes("ISO-8859-1"), "uft-8");
							} catch (UnsupportedEncodingException e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
					}
				}
				haseEncode = true;
			}
			return parameterMap;
		}
		return super.getParameterMap();
	}
	
	public String getParameter(String name)
	{
		Map<String, String[]> parameterMap = getParameterMap();
		String[] values = parameterMap.get(name);
		if(values == null) return null;
		return values[0];
		
	}
	
	@Override
	public String[] getParameterValues(String name)
	{
		Map<String, String[]> parameterMap = getParameterMap();
		String[] values = parameterMap.get(name);
		return values;
	}
}
