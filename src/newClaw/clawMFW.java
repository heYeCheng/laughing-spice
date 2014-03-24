package newClaw;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class clawMFW {
	public String dataArr="";
	public String comDataArr="";   //评论内容
	public String cityId=null;
	public openUrl oUrl=null;
	int index=0;      //该页面下的第几条信息
	public int tryTime=0;  //尝试出错次数
	public clawMFW(openUrl oUrl, String cityId){
		this.oUrl=oUrl;
		this.cityId=cityId;
		this.index=0;
	}

	public void clawUrl(String url, String level){
		Document doc = oUrl.downLoad(url, "http://www.mafengwo.cn/jd/"+cityId+"/gonglve.html");

		if(doc==null&&tryTime<10){  //出错后先暂停，然后再次尝试
			try {
				Thread.sleep((long) (20000*tryTime+Math.random()*20000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	doc = oUrl.downLoad(url, "http://www.mafengwo.cn/jd/"+cityId+"/gonglve.html");
        	tryTime++;
        }
		
		if(level=="parent"){  // 从 www.mafengwo.cn/jd/10088/0-0-0-0-0-2.html 到打开 www.mafengwo.cn/poi/466.html
			Elements hrefs = doc.getElementsByClass("shop-list").select("li.shop-item.clearfix");
			for (Element href : hrefs) {
				String localUrl="http://www.mafengwo.cn"+href.getElementsByClass("shop-title").select("a").attr("href");
				dataArr+="{\"web\":\""+href.getElementsByClass("shop-title").select("a").attr("href")+"\",\"place\":\""+href.getElementsByClass("shop-title").select("a").text()+"\",\"img\":\"\"";
				clawUrl(localUrl,"child");
				if(index==(hrefs.size()-1)){
					dataArr+="}";
					System.out.println("爬取了一个主页面："+url);		
				}else{
					dataArr+="},";
				}
				index++;
			}
		}else{
			//在 www.mafengwo.cn/poi/466.html 获取内容
			Elements divs = doc.getElementsByClass("grid-content").select("div");
			Elements tabs=divs.select("ul.tab").select("li");

			for (Element tab : tabs) {
				String textTab=tab.select("a").html();
				String id=tab.attr("data-id");

				if(id.equals("0")){   
					Elements h3s=divs.select("#detail_list_0").select("h3");
					Elements ps=divs.select("#detail_list_0").select("p");
					int tempIndex=0;
					for (Element h3sSingle : h3s) {
						String text=h3sSingle.html();
						if(text.equals("简介")){   //System.out.println(text+"1");
							dataArr+=",\"intro\":\""+ps.get(tempIndex).text()+"\"";
						}else if(text.equals("地址")){    //System.out.println(text+"2");
							dataArr+=",\"addr\":\""+ps.get(tempIndex).text()+"\"";
						}else if(text.equals("电话")){    //System.out.println(text+"3");
							dataArr+=",\"phone\":\""+ps.get(tempIndex).text()+"\"";
						}else if(text.equals("交通")){    //System.out.println(text+"4");
							dataArr+=",\"traff\":\""+ps.get(tempIndex).text()+"\"";
						}else if(text.equals("门票")){
							dataArr+=",\"price\":\""+ps.get(tempIndex).text()+"\"";
						}else if(text.equals("用时参考")){
							dataArr+=",\"time\":\""+ps.get(tempIndex).text()+"\"";
						}
						tempIndex++;
					}
				}else if(textTab.equals("最佳旅游时间")){    
					dataArr+=",\"bestT\":\""+divs.select("#detail_list_"+id).select("p").text()+"\"";
				}else if(textTab.equals("参观贴士")){
					dataArr+=",\"tips\":\""+divs.select("#detail_list_"+id).select("p").text()+"\"";
				}
			}

			dataArr+=",\"score\":\""+divs.select(".score").select("em").text()+"\"";
			dataArr+=",\"comNum\":"+divs.select(".num").select("em").text();

			//爬取对该景点的评论
			Elements comPages=divs.select("a.ti");
			int pageNums=1;  //防止只有 1 个页面的评论
			if(comPages.size()==0){
				pageNums=1;
			}else{
				pageNums=comPages.size();
			}
			for(int m=1;m<=pageNums&&m<=3;m++){
				Elements coms=null;
				if(m==1){
					coms=divs.select("div.comment-item");
				}else{
					String tempUrl=url.replace(".html", "-1-"+m+".html");
					Document docComment = null;
					docComment = oUrl.downLoad(tempUrl,url);

					coms=docComment.getElementsByClass("comment-wrap").select("div.comment-item");
				}
				clawComment(coms,m);
			}
			// 合并 评论内容
			dataArr+=",\"comment\":["+comDataArr+"]";
			comDataArr="";
		}
	}

	//负责获取 评论，只是保存在  全局函数  comDataArr 中
	public void clawComment(Elements coms,int page){
		int comIndex=0;
		for (Element com : coms) {
			Elements comInfo=com.select("div.comment-info").select("div");

			if(comIndex==0&&page==1){   
				comDataArr+="{\"star\":\""+comInfo.select("span.rank-star span").attr("class")+"\",\"comIn\":\""+comInfo.select("div.c-content").select("p").text().replace("\"", "")+"\",\"time\":\""+comInfo.select("div.add-reply").select("span").text()+"\"}";
			}else{
				comDataArr+=",{\"star\":\""+comInfo.select("span.rank-star span").attr("class")+"\",\"comIn\":\""+comInfo.select("div.c-content").select("p").text().replace("\"", "")+"\",\"time\":\""+comInfo.select("div.add-reply").select("span").text()+"\"}";
			}
			comIndex++;
		}
		//System.out.println(comDataArr);
	}
}
