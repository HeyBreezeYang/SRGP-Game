package com.master.bean.web;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class QueryParameter {
	private String start;
	private String end;
	private String serverId;
	private Object[] other;
}
