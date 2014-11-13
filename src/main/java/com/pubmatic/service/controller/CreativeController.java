package com.pubmatic.service.controller;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pubmatic.adda.domain.CreativeInfo;
import com.pubmatic.service.CreativeService;


@Controller
@RequestMapping("/creativeDiagnostic")
public class CreativeController {
	
	
	@Resource(name = "creativeService")
	private CreativeService creativeService;
	
	
	@RequestMapping(value = "/diagnose", method = RequestMethod.POST, produces="application/json")
	public @ResponseBody
	CreativeInfo add(@RequestBody CreativeInfo creative) {
		return creativeService.getCreativeInfo(creative);

	}
	
	@RequestMapping(value = "/diagnose1", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody
	boolean test() {
		try {
		 creativeService.processCreatives();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
}
