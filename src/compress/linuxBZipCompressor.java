package compress;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
/**
 * JAVA调用linux bzip2的命令行
 * 压缩组件
 * @author henry
 *
 */
public class linuxBZipCompressor {
	/** 调试选项，true打出压缩调试信息，false不打印*/
	protected static boolean is_debug=false;
	/**
	 * 返回调试位
	 * @return
	 */
	public static boolean isIs_debug() {
		return is_debug;
	}
	/**
	 * 设置调试位
	 * @param is_debug
	 */
	public static void setIs_debug(boolean is_debug) {
		linuxBZipCompressor.is_debug = is_debug;
	}
	public static void main(String[] args) throws Exception{
		setIs_debug(true);
		System.out.println(compress("scenery-10065.json", "./data/"));
		System.out.println(decompress("scenery-10065.json.bz2", "./data/"));
		System.out.println(verfifyCompress("scenery-10065.json.bz2", "./data/"));
		// bzip2 -9 -zkfv scenery-4.json && bzip2 -t scenery-4.json.bz2
//		long start=System.currentTimeMillis();
//		long end=System.currentTimeMillis();
//		System.out.println("耗时"+(end-start)*1.0/1000);
		
	}
	/**
	 * 压缩命令
	 * @param filename
	 * @param dirname
	 * @return
	 * @throws Exception
	 */
	public static boolean compress(String filename,String dirname) throws Exception{
		String verbose=" bzip2 -zkfv ";
		String out=runShell(verbose+filename,dirname,2);
		if(is_debug)
			System.out.println(out);
		if(out.endsWith("out."))
			return true;
		return false;
	}
	/**
	 * 解压缩命令
	 * @param filename
	 * @param dirname
	 * @return
	 * @throws Exception
	 */
	public static boolean decompress(String filename,String dirname) throws Exception{
		String verbose="bzip2 -dkfv ";
		String out=runShell(verbose+filename,dirname,2);
		if(is_debug)
			System.out.println(out);
		if(out.endsWith("done"))
			return true;
		return false;
	}
	
	/**
	 * 检验压缩文件完整性
	 * @param filename
	 * @param dirname
	 * @return
	 * @throws Exception
	 */
	public static boolean verfifyCompress(String filename,String dirname) throws Exception{
		String verbose="bzip2 -tv ";
		String out=runShell(verbose+filename,dirname,2);
		if(is_debug)
			System.out.println(out);
		if(out.endsWith("ok"))
			return true;
		return false;
	}
  
  
/** 
     * 运行shell 
     *  
     * @param shStr 
     *            需要执行的shell 
     * @param dir 脚本执行所在的目录
     * @param type 输出的数据类型，0是stdout，2是stderr
     * @return 
     * @throws IOException 
     */  
    public static String runShell(String shStr,String dir,int type) throws Exception {  
        StringBuilder output=new StringBuilder();
        File work_dir=new File(dir);
        Process process;
        process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",shStr},null,work_dir);
        BufferedReader br = null ;
        switch (type){
        case 0:
        	br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        	break;
        case 2:
        	br= new BufferedReader(new InputStreamReader(process.getErrorStream()));
        	break;
        default:
        	;
        }
        String line; 
        process.waitFor();
        while ((line = br.readLine()) != null){  
            output.append(line);
        }
        return output.toString(); 
    }  
}
