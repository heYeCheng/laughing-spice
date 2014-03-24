package util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class IOConduct {
	/**
	 * 调用系统的打印方法,不换行打印
	 * @param o
	 */
	public static void print(Object o){
		System.out.print(o.toString());
	}
	/**
	 * 换行打印
	 * @param o
	 */
	public static void println(Object o){
		System.out.println(o.toString());
	}
	/**
	 * 打印一空行
	 */
	public static void println(){
		System.out.println();
	}
	/**
	 * 打印数组，以这种形式输出[123, 122]
	 * @param os
	 */
	public static void printArray(Object[] os){
		System.out.println(Arrays.toString(os));
	}
	/**
	 * 打印字符串类型的集合类内容,Set<String>,打印[22, 11]
	 * @param c
	 */
	public static void printCollections(Collection<String> c){
		System.out.println(c);
	}
	/**
	 * 格式化输出，类似于C语言
	 * @param format 
	 * @param args
	 */
	public static void printf(String format,Object... args){
		System.out.printf(format,args);
	}
	
	public static void main(String... args){
		print("hello");
		printArray(new String[]{"123","122"});
		Set<String> s=new HashSet();
		s.add("11");
		s.add("22");
		printCollections(s);
		int i=0;
		for(;i<1000;i=i+3){
			if(i==945)
			printf("你是我的%c%c%c，俺喜欢",i,i+1,i+2);
//			println();
		}
		char c='α';
		printf("%d",(int)c);
	}
}
