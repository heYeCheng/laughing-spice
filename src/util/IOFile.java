package util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import static util.IOConduct.*;

/**
 * 各种文件读取的操作
 * 
 * @author 吴烈思
 * 
 */
public class IOFile {

	public static void main(String[] args) {
		Set<String> s1 = new HashSet<String>();
		outputFile("test", s1, true);
		println(s1.getClass().getName());
		// returns "java.lang.String"
		println(byte.class.getName());
		// returns "byte"
		println((new Object[3]).getClass().getName());
		// returns "[Ljava.lang.Object;"
		println((new int[3][4][5][6][7][8][9]).getClass().getName());
		// returns "[[[[[[[I"
	}

	/**
	 * 写集合类到文件
	 * 
	 * @param root
	 *            写出路径
	 * @param content
	 *            Collection集合内容
	 * @param override
	 *            是否覆盖，true为覆盖
	 */
	public static void outputFile(String root, Collection<?> content,
			boolean override) {
		StringBuilder output = new StringBuilder();
		for (Object o : content) {
			output.append(o.toString());
		}
		outputFile(root, output.toString(), override);
	}

	/**
	 * 写字符串到指定文件,一行一行写,默认输出编码是GBK
	 * 
	 * @param root
	 *            写出的路径
	 * @param content
	 *            写出的内容
	 * @param override
	 *            是否覆盖
	 * 
	 */
	public static void outputFile(String root, String content, boolean override) {
		outputFile(root, content, override, "GBK");
	}

	/**
	 * 写字符串到指定文件,一行一行写,需要指定编码
	 * 
	 * @param root
	 *            写出的路径
	 * @param content
	 *            写出的内容
	 * @param override
	 *            是否覆盖
	 * @param encode
	 *            指定编码
	 */
	public static void outputFile(String root, String content,
			boolean override, String encode) {
		FileOutputStream outSTr = null;
		BufferedWriter Buff = null;
		try {
			outSTr = new FileOutputStream(new File(root), !override);
			Buff = new BufferedWriter(new OutputStreamWriter(outSTr, encode));
			Buff.write(new String(content) + "\n");
			Buff.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				Buff.close();
				outSTr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读文件，返回字符列表 ，默认编码为UTF-8
	 * 
	 * @param s
	 *            文件名
	 * @return
	 */
	public static ArrayList<String> readFile(String s) {
		return readFile(s, "UTF-8");
	}

	/**
	 * 根据文件类型自动识别编码，并进行读取
	 * 
	 * @param s
	 *            文件路径
	 * @return
	 */
	public static ArrayList<String> readFileAutoDecoding(String s) {
		try {
			return readFile(s, codeString(s));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return null;
	}

	/**
	 * 从文件读入内容并指定编码 ,windows 下默认为GBK
	 * 
	 * @param s
	 *            文件路径
	 * @param encode
	 *            指定编码类型
	 * @return
	 */
	public static ArrayList<String> readFile(String s, String encode) {
		FileInputStream fr = null;
		BufferedReader br = null;
		try {
			fr = new FileInputStream(s);
			br = new BufferedReader(new InputStreamReader(fr, encode));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("找不到文件");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("编码不支持");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println(e.getMessage());
		}

		String line = "";
		ArrayList<String> uid = new ArrayList<String>();
		try {
			while ((line = br.readLine()) != null) {
				if (!line.matches("\\s*"))
					uid.add(line);
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			fr.close();
			br.close();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		return uid;
	}

	/**
	 * 判断文件的编码格式
	 * 
	 * @param fileName
	 *            :file
	 * @return 文件编码格式
	 * @throws Exception
	 */
	public static String codeString(String fileName) throws Exception {
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(
				fileName));
		int p = (bin.read() << 8) + bin.read();
		String code = null;
		// 其中的 0xefbb、0xfffe、0xfeff、0x5c75这些都是这个文件的前面两个字节的16进制数
		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		case 0x5c75:
			code = "ANSI|ASCII";
			break;
		default:
			code = "GBK";
		}

		return code;
	}
}
