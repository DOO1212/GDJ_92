package com.winter.app.sample;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class SampleTest {

	@Test
	void test() {
		// 0    =>    0
		// 1    =>    1
		// 2    =>    2
		// 3    =>    3
		// 4    =>    10
		// 5    =>    11
		// 6    =>    12
		// 7    =>    13
		// 8    =>    20
		// 9    =>    21
		// 10   =>    22
		// 11   =>    23
		
		int n =4;
		
		int result= n/4*10+ n%4;
		
		System.out.println(result);
		
	}
	
	@Test
	public void test2() {
		//편의점 
		int price = 34500;
		int money = 50000;
		int jandon = money-price;//15500
		
		int man=jandon/10000;
		int chon=jandon%10000/1000; //jandon - man*10000;
		System.out.println("만원 "+man+"장");
		System.out.println("천원 "+chon+"장");
		
		
	}

}
