package newClaw;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import util.IOFile;

public class spider {
	public String cityId="10121";
	public int cityPage=2;
	public int initPage=1;
	public String savePath="G:\\";
	public static openUrl oUrl=null;
	
	int index=0;      //该页面下的第几条信息
	int indexPage=1;  //在爬取地几个页面
	File file;
	/**
	 * @param args
	 */
	public spider(String cityId,String cityPage,String savePath){
		this.cityId=cityId;
		this.cityPage=Integer.valueOf(cityPage).intValue();
		this.savePath=savePath;
		this.file=new File(savePath,"scenery-"+cityId+".json");
	}
		
	public static void main(String[] args) {
		List<String> inputList=IOFile.readFile("D:\\Backup\\我的文档\\毕业设计\\开发\\wls.txt", "UTF-8");
		String outputdir="D:\\Backup\\我的文档\\毕业设计\\开发\\数据\\";
		System.getProperties().setProperty("proxySet", "true");
		System.getProperties().setProperty("http.proxyHost", "127.0.0.1");
		for (String t : inputList) {
			String[] temp=t.split("  ");
//			if(Integer.parseInt(temp[1])>2)
//				continue;
			System.out.println("正在抓取"+temp[0]);
			spider crawler = new spider(temp[0],temp[1],outputdir);
			proxyState state=new proxyState();
			oUrl=new openUrl(state);
			crawler.run();
			System.out.println(temp[0]+"抓取完成");
			//Thread.sleep(5000);
		}
//		spider sp = new spider(args[0],args[1],args[2]);
//		// TODO Auto-generated method stub
//		System.getProperties().setProperty("proxySet", "true");
//		System.getProperties().setProperty("http.proxyHost", "127.0.0.1");
//
//		//spider sp=new spider();
//		proxyState state=new proxyState();
//		oUrl=new openUrl(state);
//		sp.run();
	}
	//负责循环主页面
	public void run() {
		// TODO Auto-generated method stub
		try {//crawl();
			for(int j=initPage;j<=cityPage;j++){
				indexPage=j;
				
				clawMFW cMfw=new clawMFW(oUrl,cityId);
				cMfw.clawUrl("http://www.mafengwo.cn/jd/"+cityId+"/0-0-0-0-0-"+j+".html", "parent");
				String dataArr=cMfw.dataArr;
				try {
					if(!file.exists()){
						file.createNewFile();
					}
					BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
					if(j!=1){
						out.write(","+dataArr);
					}else{
						out.write(dataArr);
					}
					out.newLine();
					out.close();
					out = null;
					System.out.println("是第"+indexPage+"页");
					System.out.println("------------------");
					if(indexPage%3==0){
						System.out.println("中场休息");
						Thread.sleep((long) (200000*(indexPage/6)+Math.random()*25000));
					}else{
					    Thread.sleep((long) (60000+Math.random()*25000));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} 
			}
			System.out.print("已经爬完所有数据,文件放在了 "+savePath+" ，文件名为  scenery .json");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
