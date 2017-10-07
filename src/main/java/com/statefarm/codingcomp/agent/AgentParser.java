package com.statefarm.codingcomp.agent;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.statefarm.codingcomp.bean.Agent;
import com.statefarm.codingcomp.utilities.SFFileReader;

@Component
public class AgentParser {
	@Autowired
	private SFFileReader sfFileReader;

	@Cacheable(value = "agents")
	public Agent parseAgent(String fileName) {
		Agent agent = new Agent(); 
		File input = new File(fileName); 
		
		try {
			Document doc = Jsoup.parse(input, "UTF-8", "");
		} catch (IOException e) {
			System.out.println("Reading HTML failed!"); 
			e.printStackTrace();
		} 
		
		return null;
	}
}
