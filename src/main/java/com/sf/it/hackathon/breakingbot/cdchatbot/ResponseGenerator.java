package com.sf.it.hackathon.breakingbot.cdchatbot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.sf.it.hackathon.breakingbot.knowledge.KnowledgeBuilder;
import com.sf.it.hackathon.breakingbot.textmatching.TfIdfMain;

public class ResponseGenerator {
	
	public String inputCategory;
	JSONObject knowledge;
	
	public void setInputCategory(String inputCategory){
		String knowledgeFileName = null;
		this.inputCategory = inputCategory;
		
		if(inputCategory == "jenkins")
			knowledgeFileName = "JenkinsInfraKnowledge.json";
        // TBD: implement other categories too		
		
		try {
			knowledge = KnowledgeBuilder.loadKnowledge(knowledgeFileName);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String generateResponse(String sInput){
		String response = null;
		List<String>possibleResponses = new ArrayList<String>();
		if(knowledge == null) System.out.println("knowledge is null");
		Iterator<?> keys = knowledge.keys();
		
		while(keys.hasNext()){
			String resp = (String)keys.next();
			resp = resp.replace('-', ' ');
			possibleResponses.add(resp);
		}
		
		// find the key with maximum match with user input
		double maxMatch = Double.MIN_VALUE;
		String responseMatch = null;
		for (String str : possibleResponses){
			double match = TfIdfMain.cosineSimilarity(str, sInput);
			if (match > maxMatch){
				maxMatch = match;
				responseMatch = str;
			}
		}
		
	    
		
		try {
			response = (String)knowledge.get(responseMatch.replace(' ', '-').trim());
			System.out.println(response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
}
