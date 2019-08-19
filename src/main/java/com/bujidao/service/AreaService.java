package com.bujidao.service;

import java.util.List;

import com.bujidao.entity.Area;

public interface AreaService {
	String AREALIST = "arealist";
	List<Area> queryArea();
}
