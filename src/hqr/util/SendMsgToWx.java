package hqr.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class SendMsgToWx {
	
	private CloseableHttpClient httpclient;
	private HttpClientContext httpClientContext;
	
	public SendMsgToWx(CloseableHttpClient httpclient, HttpClientContext httpClientContext) {
		super();
		this.httpclient = httpclient;
		this.httpClientContext = httpClientContext;
	}

	public boolean sendMsg(String message) {
		boolean status = false;
		
		String corpId = "ww13a3890a2c0815e3";
		String corpSecret = "l0Po7Af9xblCc1YZhc5atpKGuu_0h5k4jMmOSXXHtos";
		String agentId = "1000002";
		
		String token = "";
		
		HttpGet get = new HttpGet("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpId+"&corpsecret="+corpSecret);
		try(CloseableHttpResponse cl = this.httpclient.execute(get, this.httpClientContext);) {
		    if(cl.getStatusLine().getStatusCode()==200) {
		    	JSONObject jo = JSON.parseObject(EntityUtils.toString(cl.getEntity()));
		    	String errcode = jo.get("errcode").toString();
		    	if("0".equals(errcode)) {
		    		token = (String)jo.get("access_token");
		    	}
		    }
		    else {
		    	EntityUtils.toString(cl.getEntity());
		    }
		}
		catch (Exception e) {
			System.out.println("failed to push the msg2 "+e);
		}
	    
		if(!"".equals(token)) {
			HttpPost post = new HttpPost("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+token);
			String content = message;
			//agentId=1000002
			String body2 = "{\"touser\": \"@all\",\"msgtype\" : \"text\",\"agentid\" : "+agentId+",\"text\" : {\"content\" : \""+content+"\"},\"enable_duplicate_check\" : 1, \"duplicate_check_interval\" : 3}";
			StringEntity json = new StringEntity(body2 ,ContentType.APPLICATION_JSON);
			post.setEntity(json);
			try(CloseableHttpResponse cl = httpclient.execute(post,httpClientContext);){
			    if(cl.getStatusLine().getStatusCode()==200) {
			    	JSONObject jo = JSON.parseObject(EntityUtils.toString(cl.getEntity()));
			    	String errcode = jo.get("errcode").toString();
			    	if("0".equals(errcode)) {
			    		System.out.println("msg push to user successfully");
			    	}
			    	else {
			    		System.out.println("failed to push the msg"+jo.get("errmsg"));
			    	}
			    }
			    else {
			    	EntityUtils.toString(cl.getEntity());
			    }
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		return status;
	}
	
}
