package com.winter.app.boards;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoardVO {
	
	private Integer num;
	private String name;
	private String title;
	
	public void setKind(String k) {
		this.title = k;
	}

}
