package com.pubmatic.dao;

import java.util.List;

import com.pubmatic.adda.domain.CreativeInfo;

public interface CreativeDAO {
	
	public CreativeInfo getCreative(String creativeid);
	public List <CreativeInfo> getAllCreatives ();
	
	
    /* public void insert(CreativeInfo CreativeInfo);
	
	public void insertNamedParameter(CreativeInfo CreativeInfo);
			
	public void insertBatch(List<CreativeInfo> CreativeInfo);
	
	public void insertBatchNamedParameter(List<CreativeInfo> CreativeInfo);
	
	public void insertBatchNamedParameter2(List<CreativeInfo> CreativeInfo);
			
	public void insertBatchSQL(String sql);
	
	public CreativeInfo findByCreativeInfoId(int custId);
	
	public CreativeInfo findByCreativeInfoId2(int custId);

	public List<CreativeInfo> findAll();
	
	public List<CreativeInfo> findAll2();
	
	public String findCreativeInfoNameById(int custId);
	
	public int findTotalCreativeInfo();*/

}
