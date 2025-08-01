package com.winter.app.factory;

import org.springframework.stereotype.Component;

@Component
public class GunArm implements Arm {

	@Override
	public void attack() {
		System.out.println("gun");
		
	}

}
