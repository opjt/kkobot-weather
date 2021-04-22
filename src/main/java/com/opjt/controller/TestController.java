package com.opjt.controller;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.opjt.service.MainService;

@RestController
public class TestController {

	private MainService mainService;

	public TestController() {
		mainService = new MainService();
	}
	@RequestMapping(value="/test2", method= RequestMethod.GET)
	public String test(HttpServletRequest request) {
		List listSender = new ArrayList<Object>(); 
		Map<String, Object> mapReceiver = new HashMap<String, Object>();
		
		mapReceiver.put("key_3", "value_3"); 
		mapReceiver.put("key_4", "value_4"); 
		listSender.add(mapReceiver);
		Map<String, Object> mapReceiver2 = new HashMap<String, Object>();
		
		mapReceiver2.put("key_5", "value_5"); 
		mapReceiver2.put("key_6", "value_6");
		listSender.add(mapReceiver2);
		System.out.println(listSender);
		Map<String, Object> mapReceiver3 = new HashMap<String, Object>();
		
		mapReceiver3.put("key_1", "value_1"); 
		mapReceiver3.put("key_2", "value_2");
		listSender.set(0, mapReceiver3);
		JSONObject obj = new JSONObject();
		obj.put("test", listSender);
		
		Map<String, Object> tesa = (Map<String,Object>)listSender.get(0);
		
		System.out.println(tesa.get("key_1"));
		tesa.put("key_1", "잊지마");
		listSender.set(0, tesa);
		System.out.println(obj);
		return "33";
	}
	
	
	@RequestMapping(value="/test", method= RequestMethod.GET)
	public String testByResponseBody(HttpServletRequest request) {
		
		String loca = request.getParameter("id");
		double locode[] = mainService.getloc(loca);
		int locxy[] = mainService.getxy(locode[0], locode[1]);
		
		String basetime1 = mainService.getdate(0); //0 = 한시간 단위  / 1= 3시간단위
		String basetime2 = mainService.getdate(1); //0 = 한시간 단위  / 1= 3시간단위
		System.out.println("cont" + locxy[0]);
		String url1 = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst?serviceKey=Y%2BYT4fvkAC0h8%2FqU4Eh%2BicDORXartOTimtqt4U99Tf5OfMHh8evKMYtO4pYbRHwCGxDzVUrauvVco6wGo0CiYw%3D%3D&pageNo=1&numOfRows=300&"
				+ "dataType=JSON&"
				+ basetime1
				+ "&nx="+locxy[0]+"&ny="+locxy[1];
		String url2 = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst?serviceKey=Y%2BYT4fvkAC0h8%2FqU4Eh%2BicDORXartOTimtqt4U99Tf5OfMHh8evKMYtO4pYbRHwCGxDzVUrauvVco6wGo0CiYw%3D%3D&pageNo=1&numOfRows=300&"
				+ "dataType=JSON&"
				+ basetime2 
				+ "&nx="+locxy[0]+"&ny="+locxy[1];
		String weather1 = mainService.getwe(locxy, url1);
		String weather2 = mainService.getwe(locxy, url2);
		
		
        JSONArray jj1 = mainService.jsonarray(weather1);	
        String T1H = "";
        for( int i = 0; i <jj1.size(); i++) {
        	JSONObject category = (JSONObject) jj1.get(i);
        	if(category.get("category").equals("T1H")) {
        		
        		T1H = category.get("obsrValue").toString();
        	}
        	
        }
        System.out.println("T1H : " + T1H);	
        JSONArray jj2 = mainService.jsonarray(weather2);	
        
        
        String list[] = {"POP","TMX","TMN","SKY","PTY"};
        String skyv[] = {"","맑음","","구름많음","흐림"};
        String emoji[] = {"","☀️","🌥️","☁️","","🌧️","🌧️","🌨️","🌧️","🌧️","🌧️","🌨️"};
        //
        String rainv[] = {"", "비", "비/눈", "눈", "소나기", "빗방울", "빗방울/눈날림", "눈날림"};
        Map<String, Integer> woa = new HashMap<>();
        JSONArray weekjs = new JSONArray();
        List Listitem = new ArrayList<Object>(); 
//        List listSender = new ArrayList<Object>(); 
//		Map<String, Object> mapReceiver = new HashMap<String, Object>();
        int count = 0;
        for(int i=0; i <jj2.size(); i++) {
        	JSONObject item = (JSONObject) jj2.get(i);
        	if(woa.get(item.get("fcstDate")) == null) {
        		woa.put((String) item.get("fcstDate"), count);
        		//JSONObject newmap = new JSONObject();
        		Map<String, Object> newmap = new HashMap<String, Object>();
                newmap.put("date", (String) item.get("fcstDate"));
                newmap.put("POP", "NULL");
                newmap.put("TMX", "NULL");
                newmap.put("TMN", "NULL");
                newmap.put("SKY", "NULL");
                newmap.put("PTY", "NULL");
                newmap.put("emo", "NULL");
                Listitem.add(newmap);
                
                System.out.println("count :" +count);
        		count++;
        		
        	}
        	
        	if(Arrays.asList(list).contains(item.get("category"))) {
        		//System.out.println(item.get("category"));
        		//System.out.println(woa.get(item.get("fcstDate")));
        		
        		int dateid = woa.get(item.get("fcstDate"));
        		Map<String, Object> var = (Map<String,Object>)Listitem.get(dateid);

        		String var1 = (String) var.get(item.get("category"));
        		//System.out.println(val1 + " " + dateid + " ");
        		if(var1.equals("NULL")) {
//        			System.out.println(dateid);
//        			System.out.println(item.get("category"));
        			var1 = (String) item.get("fcstValue");
        			var.put((String) item.get("category"), var1);
        			Listitem.set(dateid, var);
        			
        		}
        		if(item.get("fcstTime").equals("1200")) {
        			
        			var1 = (String) item.get("fcstValue");
        			var.put((String) item.get("category"), var1);
        			Listitem.set(dateid, var);
        		}
        		
        	}
        	
        }
       
        System.out.println("count : " + count );
        JSONObject retobj = new JSONObject();
        for(int i = 0; i < count; i++) {
        	Map<String, Object> vas = (Map<String,Object>)Listitem.get(i);
        	
        	vas.put("emo", emoji[Integer.parseInt((String) vas.get("SKY"))]);
        	
        	int vc = Integer.parseInt((String) vas.get("PTY")) +4;
        	//System.out.println(vc);
        	if(emoji[vc].equals("") == false) {
        		vas.put("emo", emoji[vc]);
        	}
        	vas.put("SKY", skyv[Integer.parseInt((String) vas.get("SKY"))]);
        	vas.put("PTY", rainv[Integer.parseInt((String) vas.get("PTY"))]);
        	retobj.put(i,vas);
           
       }
        retobj.put("now", T1H);
        System.out.println(retobj);
	     System.out.println(jj2);
//		String jsontest = mainService.getjson(url);
//		System.out.println(jsontest);
		
		String message = mainService.test(request);
        
        
		//return "test";
		return retobj.toString();
	}

}


