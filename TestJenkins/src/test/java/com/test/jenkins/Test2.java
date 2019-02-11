package com.test.jenkins;

import org.testng.annotations.Test;

public class Test2 {
	
	@Test(priority=1)
	public void test1(){
		
		System.out.println("In second class.Priority1");
	}
	
	
	@Test(dependsOnMethods="test1")
	public void test2(){
		
		System.out.println("In second class.Priority2");
	}
	
	@Test(dependsOnMethods="test2")
	public void test3(){
		
		System.out.println("In second class.Priority3");
	}

}
