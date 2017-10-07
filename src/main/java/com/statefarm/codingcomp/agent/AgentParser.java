package com.statefarm.codingcomp.agent;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.statefarm.codingcomp.bean.Agent;
import com.statefarm.codingcomp.bean.Product;
import com.statefarm.codingcomp.utilities.SFFileReader;

@Component
public class AgentParser {
	@Autowired
	private SFFileReader sfFileReader;

	@Cacheable(value = "agents")
	public Agent parseAgent(String fileName) {
		Agent agent = new Agent(); 
		File input = new File(fileName);
		Document doc;
		
		try {
			doc = Jsoup.parse(input, "UTF-8", "");
			setAgentName(agent, doc);
		} catch (IOException e) {
			System.out.println("Reading HTML failed!"); 
			e.printStackTrace();
		}
		
		return agent;
	}
	
	private Set<Product> getProducts (Document doc) {
		Set<Product> products = new HashSet<Product>(); 
		
		return products; 
	}
	
	private void setAgentName (Agent agent, Document doc) {
		// there is only one name, so we use first()
		agent.setName(doc.getElementsByAttributeValue("itemprop", "name").first().text());
	}
}
