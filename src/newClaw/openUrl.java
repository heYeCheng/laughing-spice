package newClaw;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class openUrl {
	public int currentProxy=1;
    public proxyState state=null;
    
	public openUrl(proxyState state){
		this.state=state;
		this.currentProxy=state.getProxy();
	}

	//负责获取页面，并返回   Document  格式
	public Document downLoad(String url, String refer) {
		Document doc = null ;
		double clawTime = 0;

		clawTime=Math.random()*15000*Math.random();
		if (clawTime<5000){
			clawTime+=6000;
		}
		try {
			Thread.sleep((long) (clawTime));
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.print("当前爬取间隔时间为："+clawTime+" 的 ＵＲＬ："+url);
		//Thread.sleep((long) (Math.random()*10000));   // 设置随机时间，减少是爬虫的特征
		try {
			if(Math.random()<0.3&&state.getFlag1()){
				System.out.println("使用   本机 代理");
				System.getProperties().setProperty("http.proxyPort", "49242");
				doc= Jsoup.connect(url)  
						.userAgent("Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0")//设置urer-agent  
						.referrer(refer)
						.timeout(70000)//设置连接超时  
						.get();
			}else{
				System.out.println("使用   goagent  代理");
				System.getProperties().setProperty("http.proxyPort", "8087");
				doc= Jsoup.connect(url)  
						.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36")//设置urer-agent  
						.referrer(refer)
						.timeout(70000)//设置连接超时  
						.get();
			} 
		}catch (IOException e) {
			if(currentProxy==1){
				state.setFlag1(false);
			}else{
				state.setFlag2(false);
			}
			System.out.println("注意，已经爬取出错！！！！！！！！程序进入休眠，1  分钟后自动启动  当前爬取 的 ＵＲＬ："+url);
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				Thread.sleep((long) (60*1000));
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return doc;
	}
}
