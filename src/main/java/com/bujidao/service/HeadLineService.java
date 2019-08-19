package com.bujidao.service;

import java.util.List;

import com.bujidao.entity.HeadLine;

public interface HeadLineService {
	String HLLISTKEY = "headlinelist";
	List<HeadLine> getHeadLineList(HeadLine headLineCondition);
}
