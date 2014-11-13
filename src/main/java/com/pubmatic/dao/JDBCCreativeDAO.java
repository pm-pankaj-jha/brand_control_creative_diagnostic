package com.pubmatic.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;







import com.pubmatic.adda.domain.CreativeInfo;

public class JDBCCreativeDAO extends JdbcDaoSupport implements CreativeDAO {

	@Override
	public CreativeInfo getCreative(String creativeid) {
		String sql = "SELECT job_url FROM brand_control_config_db.creative_scan_job order by last_update_time desc limit 1";
		return null;
	 
		//	getJdbcTemplate().update(sql, new Object[] { customer.getCustId(),customer.getName(),customer.getAge()  
	}

	@Override
	public List<CreativeInfo> getAllCreatives() {
		String query = "SELECT job_url FROM brand_control_config_db.creative_scan_job order by last_update_time desc limit 1";
		List<CreativeInfo> creativeList = new ArrayList<CreativeInfo>();
		 //using RowMapper anonymous class, we can create a separate RowMapper for reuse
       /* CreativeInfo creativeInfo = (CreativeInfo) getJdbcTemplate().query(query,  new RowMapper<CreativeInfo>(){
 
			@Override
			public CreativeInfo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				CreativeInfo creativeInfo = new CreativeInfo();
				creativeInfo.setUrl(rs.getString("job_url"));
				return creativeInfo;
			}});*/
		
		 List<Map<String,Object>> creatives = getJdbcTemplate().queryForList(query);
         
	        for(Map<String,Object> creativeRow : creatives){
	        	CreativeInfo creativeInfo = new CreativeInfo();
	        	System.out.println(""+creativeRow.get("job_url"));
	        	creativeInfo.setUrl(""+creativeRow.get("job_url"));	           
	        	creativeList.add(creativeInfo);
	        }
	        return creativeList;
	}

	
}
