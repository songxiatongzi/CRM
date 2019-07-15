package com.bjpowernode.utils;

import java.util.UUID;

public class UUIDUtil {
	
	public static String getUUID(){

		//将uuid中间的-替换掉
		return UUID.randomUUID().toString().replaceAll("-","");
		
	}
	
}
