package com.statefarm.codingcomp.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.statefarm.codingcomp.bean.Agent;
import com.statefarm.codingcomp.bean.Office;
import com.statefarm.codingcomp.bean.USState;
import com.statefarm.codingcomp.utilities.SFFileReader;

/**
 * Only US Agents are applicable since State Farm is currently in the process of
 * selling off its Canadian business.
 */
@Component
public class AgentLocator {
	@Autowired
	private AgentParser agentParser;

	@Autowired
	private SFFileReader sfFileReader;

	/**
	 * Find agents where the URL of their name contains the firstName and
	 * lastName For instance, Tom Newman would search for "Tom-" and "-Newman"	
	 * in the URL.
	 * 
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public List<Agent> getAgentsByName(String firstName, String lastName) {
		List <Agent> agents = new ArrayList<Agent>(); 
		List <String> agentFiles = sfFileReader.findAgentFiles(); 
		
		for(String str : agentFiles) {
			if( (str.contains(firstName + "-")) && (str.contains("-" + lastName))) {
				agents.add(agentParser.parseAgent(str)); 
			}
		}
		
		return agents; 
	}

	/**
	 * Find agents by state.
	 * 
	 * @param state
	 * @return
	 */
	public List<Agent> getAgentsByState(USState state) {
		List <Agent> agents = getAllAgents(); 
		List <Agent> agentsByState = new ArrayList<Agent>(); 
		
		for(Agent a : agents) {
			for(Office o : a.getOffices()) {
				if(o.getAddress().getState() == state) {
					agentsByState.add(a); 
				}
			}
		}
		
		return agentsByState; 
	}

	public List<Agent> getAllAgents() {
		List<Agent> agents = new ArrayList<Agent>(); 
		List<String> agentFiles = sfFileReader.findAgentFiles(); 
		
		for(String str : agentFiles) {
			agents.add(agentParser.parseAgent(str)); 
		}
		
		return agents; 
	}

	public Map<String, List<Agent>> getAllAgentsByUniqueFullName() {
		List <Agent> agents = getAllAgents(); 
		Map <String, List<Agent>> map = new HashMap <String, List<Agent>> (); 
		
		for(Agent a : agents) {
			System.out.println(a.getName());
			if(map.containsKey(a.getName())) {
				List <Agent> _temp = map.get(a.getName()); 
				_temp.add(a); 
				map.put(a.getName(), _temp); 
			} else {
				List <Agent> _temp = new ArrayList <Agent> (); 
				_temp.add(a); 
				map.put(a.getName(), _temp); 
			}
		}
		
		return map; 
	}

	public String mostPopularFirstName() {
		List <Agent> agents = getAllAgents(); 
		Map <String, Integer> map = new HashMap<String, Integer>(); 
		String popularName = ""; 
		
		for(Agent a : agents) { 
			String firstname = a.getName().split(" ")[0]; 
		}
		
		return popularName; 
	}

	public String mostPopularLastName() {
		return null;

	}

	public String mostPopularSuffix() {
		return null;

	}
}
