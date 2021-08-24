package hqr;

import java.util.Properties;

import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;

import hqr.btschool.BtschoolSign;
import hqr.nicept.NiceptSign;
import hqr.pttime.PttimeSign;
import hqr.util.Brower;
import hqr.util.SendMsgToWx;

public class MainApp {
	
	public static void main(String[] args) {
		
		HttpClientContext httpClientContext = Brower.getHttpClientContext();
		try(CloseableHttpClient httpclient =  Brower.getCloseableHttpClient();){
			//get config cookie
			Properties prop = new Properties();
			prop.load(MainApp.class.getResourceAsStream("/config.prop"));
			
			String pttimeCookie = (String)prop.get("pttime_cookie");
			String btschoolCookie = (String)prop.get("btschool_cookie");
			String niceptCookie = (String)prop.get("nicept_cookie");
			
			SendMsgToWx send = new SendMsgToWx(httpclient, httpClientContext);
			
			//pttime
			System.out.println("开始进行pttime的签到...");
			PttimeSign ps = new PttimeSign(httpclient, httpClientContext, pttimeCookie);
			if(ps.execute()) {
				send.sendMsg("PTtime签到成功\n结果:"+ps.getMsg());
			}
			else {
				send.sendMsg("Pttime签到失败");
			}
			System.out.println("结果:"+ps.getMsg());
			
			//btschool
			System.out.println("开始进行btschool的签到...");
			BtschoolSign bs = new BtschoolSign(httpclient, httpClientContext, btschoolCookie);
			if(bs.execute()) {
				send.sendMsg("Btschool签到成功\n结果:"+bs.getMsg());
			}
			else {
				send.sendMsg("Btschool签到失败");
			}
			System.out.println("结果:"+bs.getMsg());
			
			//nicept
			System.out.println("开始进行nicept的签到");
			NiceptSign ns = new NiceptSign(httpclient, httpClientContext, niceptCookie);
			if(ns.execute()) {
				send.sendMsg("Nicept签到成功\n结果:"+ns.getMsg());
			}
			else {
				send.sendMsg("Nicept签到失败");
			}
			System.out.println("结果:"+ns.getMsg());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
