package compress;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/**
 * 2014-3-17
 * 调用apache的开源压缩工具bzip2进行文本压缩
 * 缺点:对于大文件时间(M级别的)太久了,shell下bzip2执行都差不多1s可以完成,java要几十秒
 * 最后觉得gzip，压缩率比bzip2低，但是有java的原生实现，能有较快的运行速度,
 * 选择gzip比zip跟其他apache commons的包有更好的综合性能
 */
import org.apache.commons.compress.compressors.bzip2.*;
public class testApacheCompress {
	public static final int BUFFER = 1024;  
    public static final CharSequence EXT = ".gz";
    public static final int BLOCKSIZE=2; //块大小选择
	public static void main(String[] args) throws Exception{
		String path="/media/working/Java调试/毕业设计/data/scenery-10065.json";
//		FileInputStream fin = new FileInputStream(path+"scenery-4.json");
//		BufferedInputStream in = new BufferedInputStream(fin);
//		FileOutputStream out = new FileOutputStream(path+"scenery-4-apache.json.bz2");
//		
//		BZip2CompressorOutputStream bzOut = new BZip2CompressorOutputStream(out);
//		int buffersize=1024;
//		final byte[] buffer = new byte[buffersize];
//		int n = 0;
//		while (-1 != (n = in.read(buffer,0,buffersize))) {
//		    bzOut.write(buffer, 0, n);
//		}
//		bzOut.finish();
//		bzOut.flush();
//		bzOut.close();
//		
//		in.close();
//		out.close();
		long start=System.currentTimeMillis();
		testApacheCompress.compress(path, false);
		long end=System.currentTimeMillis();
		System.out.println("耗时"+(end-start)*1.0/1000);
		testApacheCompress.compress_evaluate(path, path+EXT);
		
//		testApacheCompress.decompress(path+"scenery-4-apache.json.bz2", false);
	}
  
	/**
	 * 数据压缩结果评价:时间+压缩率
	 * @param file1 
	 * @param file2
	 */
	public static void compress_evaluate(String file1,String file2){
		long len1=new File(file1).length();
		long len2=new File(file2).length();
		System.out.println(len1*1.0/len2+", "+(1-len2*1.0/len1)*100+"% saved,"+len1+" in, "+len2+" out.");
//		3.225:1,  2.481 bits/byte, 68.99% saved, 100355 in, 31119 out.
	}
	
    /** 
     * 数据压缩 
     *  
     * @param data 未压缩数据
     * @return 
     * @throws Exception 
     */  
    public static byte[] compress(byte[] data) throws Exception {  
        ByteArrayInputStream bais = new ByteArrayInputStream(data);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
  
        // 压缩  
        bzip2Compress(bais, baos);  
  
        byte[] output = baos.toByteArray();  
  
        baos.flush();  
        baos.close();  
  
        bais.close();  
  
        return output;  
    }  
  
    /** 
     * 文件压缩 
     *  
     * @param file 
     * @throws Exception 
     */  
    public static void compress(File file) throws Exception {  
        compress(file, true);  
    }  
  
    /** 
     * 文件压缩 
     *  
     * @param file 未压缩文件
     * @param delete 
     *            是否删除原始文件 
     * @throws Exception 
     */  
    public static void compress(File file, boolean delete) throws Exception {  
        FileInputStream fis = new FileInputStream(file);  
        FileOutputStream fos = new FileOutputStream(file.getPath() + EXT);  
        if(EXT.equals(".zip"))
        	zipCompress(fis, fos, file.getPath()+EXT);
        else if(EXT.equals(".bz2"))
        	 bzip2Compress(fis, fos);
        else if(EXT.equals(".gz"))
        	gzipCompress(fis, fos);
        else
        	;
        fis.close();  
        fos.flush();  
        fos.close();  
  
        if (delete) {  
            file.delete();  
        }  
    }  
  /**
   *  snappy 适用于超大规模数据，压缩速度比较块，参考<a href="">http://www.infoq.com/cn/news/2011/04/Snappy</a>
   * snappy 压缩
   * @throws Exception
   */
    public static void snappyCompress(InputStream is, OutputStream os) throws Exception{
    	
    }
    /** 
     * bzip2数据压缩 
     *  
     * @param is 
     * @param os 
     * @throws Exception 
     */  
    public static void bzip2Compress(InputStream is, OutputStream os)  
            throws Exception {  
        BZip2CompressorOutputStream gos = new BZip2CompressorOutputStream(os,BLOCKSIZE);  
  
        int count;  
        byte data[] = new byte[BUFFER];  
        while ((count = is.read(data, 0, BUFFER)) != -1) {  
            gos.write(data, 0, count);  
        }  
  
        gos.finish();  
  
        gos.flush();  
        gos.close();  
    }  
  
    /**
     * zip 数据压缩
     * @param is
     * @param os
     * @param filename
     * @throws Exception
     */
    //ZIPOutputStream
    public static void zipCompress(InputStream is, OutputStream os,String filename)  
            throws Exception {  
    	ZipOutputStream gos = new ZipOutputStream(os);  
    	ZipEntry ze = new ZipEntry(filename);
        gos.putNextEntry(ze);
        int count;  
        byte data[] = new byte[BUFFER];  
        while ((count = is.read(data, 0, BUFFER)) != -1) {  
            gos.write(data, 0, count);  
        }
        gos.closeEntry();
        gos.finish();  
        gos.flush();  
        gos.close();  
    }  
    
    /**
     * GZip压缩
     * @param is
     * @param os
     * @throws Exception
     */
	public static void gzipCompress(InputStream is, OutputStream os)
			throws Exception {
		GZIPOutputStream gos = new GZIPOutputStream(os);

		int count;
		byte data[] = new byte[BUFFER];
		while ((count = is.read(data, 0, BUFFER)) != -1) {
			gos.write(data, 0, count);
		}

		gos.finish();

		gos.flush();
		gos.close(); 
	}
    /** 
     * 文件压缩 
     *  
     * @param path 
     * @throws Exception 
     */  
    public static void compress(String path) throws Exception {  
        compress(path, true);  
    }  
  
    /** 
     * 文件压缩 
     *  
     * @param path 
     * @param delete 
     *            是否删除原始文件 
     * @throws Exception 
     */  
    public static void compress(String path, boolean delete) throws Exception {  
        File file = new File(path);  
        compress(file, delete);  
    }  
  
    /** 
     * 数据解压缩 
     *  
     * @param data 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decompress(byte[] data) throws Exception {  
        ByteArrayInputStream bais = new ByteArrayInputStream(data);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
  
        // 解压缩  
  
        bzip2Decompress(bais, baos);  
  
        data = baos.toByteArray();  
  
        baos.flush();  
        baos.close();  
  
        bais.close();  
  
        return data;  
    }  
  
    /** 
     * 文件解压缩 
     *  
     * @param file 
     * @throws Exception 
     */  
    public static void decompress(File file) throws Exception {  
        decompress(file, true);  
    }  
  
    /** 
     * 文件解压缩 
     *  
     * @param file 
     * @param delete 
     *            是否删除原始文件 
     * @throws Exception 
     */  
    public static void decompress(File file, boolean delete) throws Exception {  
        FileInputStream fis = new FileInputStream(file);  
        FileOutputStream fos = new FileOutputStream(file.getPath().replace(EXT,  
                ""));  
        bzip2Decompress(fis, fos);  
        fis.close();  
        fos.flush();  
        fos.close();  
  
        if (delete) {  
            file.delete();  
        }  
    }  
  
    /** 
     * 数据解压缩 
     *  
     * @param is 
     * @param os 
     * @throws Exception 
     */  
    public static void bzip2Decompress(InputStream is, OutputStream os)  
            throws Exception {  
  
        BZip2CompressorInputStream gis = new BZip2CompressorInputStream(is);  
        
        int count;  
        byte data[] = new byte[BUFFER];  
        while ((count = gis.read(data, 0, BUFFER)) != -1) {  
            os.write(data, 0, count);  
        }  
  
        gis.close();  
    }  
  
    /** 
     * 文件解压缩 
     *  
     * @param path 
     * @throws Exception 
     */  
    public static void decompress(String path) throws Exception {  
        decompress(path, true);  
    }  
  
    /** 
     * 文件解压缩 
     *  
     * @param path 
     * @param delete 
     *            是否删除原始文件 
     * @throws Exception 
     */  
    public static void decompress(String path, boolean delete) throws Exception {  
        File file = new File(path);  
        decompress(file, delete);  
    }  
}
