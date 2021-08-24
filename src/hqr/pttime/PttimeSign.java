package hqr.pttime;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PttimeSign {
	private CloseableHttpClient httpclient;
	private HttpClientContext httpClientContext;
	private String cookie;
	private String msg;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public PttimeSign(CloseableHttpClient httpclient, HttpClientContext httpClientContext, String cookie) {
		super();
		this.httpclient = httpclient;
		this.httpClientContext = httpClientContext;
		this.cookie = cookie;
	}
	public boolean execute() {
		boolean status = false;
		HttpGet get = new HttpGet("https://www.pttime.org/attendance.php");
		get.addHeader("authority", "www.pttime.org");
		get.addHeader("pragma", "no-cache");
		get.addHeader("cache-control", "no-cache");
		get.addHeader("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"");
		get.addHeader("sec-ch-ua-mobile", "?0");
		get.addHeader("origin", "https://www.pttime.org");
		get.addHeader("upgrade-insecure-requests", "1");
		get.addHeader("dnt", "1");
		//get.addHeader("content-type", "application/x-www-form-urlencoded");
		get.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
		get.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		get.addHeader("sec-fetch-site", "same-origin");
		get.addHeader("sec-fetch-mode", "navigate");
		get.addHeader("sec-fetch-user", "?1");
		get.addHeader("sec-fetch-dest", "document");
		get.addHeader("referer", "https://www.pttime.org/index.php");
		get.addHeader("accept-language", "zh-CN,zh;q=0.9");
		get.addHeader("cookie", cookie);

		try(CloseableHttpResponse cl = httpclient.execute(get, httpClientContext)){
			if(cl.getStatusLine().getStatusCode()==200) {
				Document mainHtml = Jsoup.parse(EntityUtils.toString(cl.getEntity()));
				try {
					Element outTb = mainHtml.getElementById("info_block");
					Element div2 = outTb.getElementsByTag("div").get(1);
					Element point = div2.getElementsByTag("span").get(6);
					msg = point.text();
				}
				catch (Exception e) {
				}
				//whatever get the point or not, consider sign successfully
				status = true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}
}
