package com.opjt.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MainDao {

	public String test(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String a = request.getParameter("a");
		return "메롱메롱" + a;
	}

	public String getjson(String url) {

		return "asd";
	}

	public double[] getloc(String loca) {
		// TODO Auto-generated method stub

		String customerKey = "KakaoAK";
		String customerSecret = "25e1aa77ee0ff9697955627ddfaac0f6";
		String plainCredentials = loca;
		String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
		String encod = null;
		try {
			encod = URLEncoder.encode(loca, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// System.out.println(encod);
		String url1 = "https://dapi.kakao.com/v2/local/search/address.json" + "?query=" + encod;
		// Create authorization header
		String authorizationHeader = "Basic " + base64Credentials;
		authorizationHeader = "KakaoAK " + customerSecret;
		String resut = null;
		try {

			URL url = new URL(url1);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", authorizationHeader);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			resut = response.toString();

		} catch (Exception e) {
			System.out.println(e);
		}

		JSONParser parser = new JSONParser();
		Object obj = null;

		resut = resut.replace("'", "\"");

		try {
			obj = parser.parse(resut);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONObject jsonObj = (JSONObject) obj;
		JSONArray jsr1 = (JSONArray) jsonObj.get("documents");
		JSONObject jsr2 = (JSONObject) jsr1.get(0);
		JSONObject jsr3 = (JSONObject) jsr2.get("address");

		// System.out.println(resut);

		String x = (String) jsr3.get("x");
		String y = (String) jsr3.get("y");

		double test[] = { Double.parseDouble(x), Double.parseDouble(y) };
//		test[0] = Float.parseFloat(x);
//		test[1] = Float.parseFloat(y);
		return test;
	}

	public int[] getxy(double f, double g) {
		double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)
        HashMap<String, Object> resultMap = new HashMap<>();
        int[] resultxy = {1,2};
        try {
			 double DEGRAD = Math.PI / 180.0;
			 double re = RE / GRID;
			 double slat1 = SLAT1 * DEGRAD;
			 double slat2 = SLAT2 * DEGRAD;
			 double olon = OLON * DEGRAD;
			 double olat = OLAT * DEGRAD;			
			 double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
			 sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
			 
			 double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
			 sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
			 double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
			 ro = re * sf / Math.pow(ro, sn);
			 
			 double lat = (double) g;
			 double lon = (double) f;
			 double ra = Math.tan(Math.PI * 0.25 + (lat) * DEGRAD * 0.5);
			 ra = re * sf / Math.pow(ra, sn);
			 double theta = lon * DEGRAD - olon;
			 if (theta > Math.PI)
				 theta -= 2.0 * Math.PI;
			 if (theta < -Math.PI)
				 theta += 2.0 * Math.PI;
			 
			 theta *= sn;
			 int nx = (int) Math.floor(ra * Math.sin(theta) + XO + 0.5);
			 
			 resultMap.put("nx", Math.floor(ra * Math.sin(theta) + XO + 0.5));
			 resultMap.put("ny", Math.floor(ro - ra * Math.cos(theta) + YO + 0.5));
			 double realx = (double) resultMap.get("nx");
			 double realy = (double) resultMap.get("ny");
			 resultxy[0] = (int) realx;
			 resultxy[1] = (int) realy;
			 

		} catch (Exception e) {
			e.printStackTrace();
		}
        //System.out.println(resultxy[0]);
        return resultxy;

	}

	public String getdate(int i) {
		// TODO Auto-generated method stub
		String base_date = "";
		String base_time = "";
		StringBuffer returnSB = new StringBuffer();
		if(i == 0 ) {
			//System.out.println("0입니다");
			
			SimpleDateFormat Format = new SimpleDateFormat("mm", Locale.KOREA);
			Date time = new Date();
			Calendar cal = Calendar.getInstance();
			
			//System.out.println(Format.format(time));
			if(Integer.parseInt(Format.format(time)) < 31) {
				//System.out.println("31미만");
				Format = new SimpleDateFormat("H", Locale.KOREA);
				if(Integer.parseInt(Format.format(time)) == 0) {
					//System.out.println("01미만");
					Format = new SimpleDateFormat("YYYYMMdd", Locale.KOREA);
			        
			        cal.add(Calendar.DATE, -1);
			        String timedate = Format.format(cal.getTime());
			        //base_date=20210420&base_time=2300
			        base_date = timedate;

				} else {
					Format = new SimpleDateFormat("YYYYMMdd", Locale.KOREA);
					String timedate = Format.format(time);
					base_date = timedate;
				}
				
				Format = new SimpleDateFormat("HHmm", Locale.KOREA);
				cal = Calendar.getInstance();
				cal.add(Calendar.HOUR, -1);
				String timedate = Format.format(cal.getTime());
				//System.out.println("timedate" + timedate);
				base_time = timedate;
				
			} else {
				Format = new SimpleDateFormat("YYYYMMdd", Locale.KOREA);
				time = new Date();
				String timedate = Format.format(cal.getTime());
				base_date = timedate;
				
				Format = new SimpleDateFormat("HHmm", Locale.KOREA);
				time = new Date();
				timedate = Format.format(cal.getTime());
				base_time = timedate;
			}
		} else if (i == 1) {
			SimpleDateFormat Format = new SimpleDateFormat("H", Locale.KOREA);
			Date time = new Date();
			int hour = Integer.parseInt(Format.format(time));
			Calendar cal = Calendar.getInstance();
			
			if(hour == 0 || hour == 1 || hour == 2 ) {
				Format = new SimpleDateFormat("YYYYMMdd", Locale.KOREA);
		        
		        cal.add(Calendar.DATE, -1);
		        String timedate = Format.format(cal.getTime());
		        //base_date=20210420&base_time=2300
		        base_date = timedate;
			} else {
				 Format = new SimpleDateFormat("YYYYMMdd", Locale.KOREA);
				 String timedate = Format.format(time);
				 base_date = timedate;
			}
			String houra = "";
			if( hour == 3 || hour == 4 || hour == 5 ) {
		        houra = "0200";
		    }
		    if( hour == 6 || hour == 7 || hour == 8 ) {
		        houra = "0500";
		    }
		    if( hour == 9 || hour == 10 || hour == 11 ) {
		        houra = "0800";
		    }
		    if( hour == 12 || hour == 13 || hour == 14 ) {
		        houra = "1100";
		    }
		    if( hour == 15 || hour == 16 || hour == 17 ) {
		        houra = "1400";
		    }
		    if( hour == 18 || hour == 19 || hour == 20 ) {
		        houra = "1700";
		    }
		    if( hour == 21 || hour == 22 || hour == 23 ) {
		        houra = "1700";
		    }
		    if( hour == 0 || hour == 1 || hour == 2 ) {
		        houra = "2300";
		    }
		    base_time = houra;
			
		}
		
        
        String returnDate = "base_date="+base_date+"&base_time="+base_time;
        System.out.println(returnDate);
		return returnDate;
	}

	public String getwe(int[] locxy, String url1) {
		String resut = "";
		
        try {
            URL url = new URL(url1);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            //con.setRequestProperty("Authorization", header);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            
            
            resut = response.toString();
            
        } catch (Exception e) {
            System.out.println(e);
        }
		return resut;
	}

	public JSONArray jsonarray(String text) {
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
        
        Object obj1 = null;

     // 따옴표 변환 필요
        text = text.replace("'", "\"");
	
	     try {
	    	 obj1 = parser.parse(text);
	      
	     } catch (ParseException e) {
	    	 e.printStackTrace();
	     }
	     JSONObject jsonObj1 = (JSONObject) obj1;
	     JSONObject jsr1 = (JSONObject) jsonObj1.get("response");
	     jsr1 = (JSONObject) jsr1.get("body");
	     jsr1 = (JSONObject) jsr1.get("items");
	     JSONArray jsra1 =  (JSONArray) jsr1.get("item");
	     
	     return jsra1;
	}
	

}
