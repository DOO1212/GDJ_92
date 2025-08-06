package com.winter.app.board.sample;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SampleTest {

	@Test
	void test() {

		int money = 50000;
		int price = 34500;
		
		int a = money-price;
		
		int man = a/10000;	
		int cheon = (a-(man*10000))/1000;
		int baeck = (a-(man*10000)-(cheon*1000))/100;

				
		System.out.println("만원" + man + "장");
		System.out.println("천원" + cheon + "장");
		System.out.println("백원" + baeck + "장");
	}

}
