package com.statefarm.codingcomp.agent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.statefarm.codingcomp.bean.Address;
import com.statefarm.codingcomp.bean.Agent;
import com.statefarm.codingcomp.bean.Office;
import com.statefarm.codingcomp.bean.USState;
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
			setOffices(agent, doc);
			setProducts(agent, doc); 
		} catch (IOException e) {
			System.out.println("Reading HTML failed!"); 
			e.printStackTrace();
		}
		
		return agent;
	}
	
	private void setProducts (Agent agent, Document doc) {
		Set<Product> products = new HashSet<Product>(); 
		
		Elements e = doc.getElementsByAttributeValue("itemprop", "description").select("li");
		for(int i = 0; i < e.size(); i++) {
			products.add(Product.fromValue(e.get(i).text())); 
		}
		
		agent.setProducts(products);
	}
	
	private void setAgentName (Agent agent, Document doc) {
		// there is only one name, so we use first()
		agent.setName(doc.getElementsByAttributeValue("itemprop", "name").first().text());
	}
	
	private void setOffices (Agent agent, Document doc) {
		Elements addresses = doc.getElementsByAttributeValue("itemprop", "address");
		List<Office> offices = new ArrayList<Office>();
		for (Element addressElem : addresses) {
			Address address = new Address();
			
			// set street address
			String[] fullAddress = addressElem.getElementsByAttributeValueContaining("id", "locStreetContent").html().split("<br>");
			address.setLine1(fullAddress[0]);
			if (fullAddress.length > 1) {
				address.setLine2(fullAddress[1]);
			}
			
			// set city
			String city = addressElem.getElementsByAttributeValue("itemprop", "addressLocality").text();
			address.setCity(city.substring(0, city.length() - 1)); // strip comma off of end
			
			// set state
			address.setState(USState.fromValue(addressElem.getElementsByAttributeValue("itemprop", "addressRegion").text()));
			
			// set postal code
			String postalCode = addressElem.getElementsByAttributeValue("itemprop", "postalCode").text();
			address.setPostalCode(postalCode.substring(0, 5));
			
			Office o = new Office();
			o.setAddress(address);
			offices.add(o);
		}
	}
}
