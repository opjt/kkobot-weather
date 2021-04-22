package com.opjt.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;

import com.opjt.dao.MainDao;

public class MainService {
	private MainDao mainDao;
	public MainService() {
		mainDao = new MainDao();
	}
	public String test(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return mainDao.test(request);
		
	}
	public String getjson(String url) {
		// TODO Auto-generated method stub
		return mainDao.getjson(url);
	}
	public double[] getloc(String loca) {
		// TODO Auto-generated method stub
		
		return mainDao.getloc(loca);
	}
	public int[] getxy(double f, double g) {
		// TODO Auto-generated method stub
		return mainDao.getxy(f,g);
	}
	public String getdate(int i) {
		// TODO Auto-generated method stub
		return mainDao.getdate(i);
	}
	public String getwe(int[] locxy, String url) {
		// TODO Auto-generated method stub
		return mainDao.getwe(locxy, url);
	}
	public JSONArray jsonarray(String text) {
		// TODO Auto-generated method stub
		return mainDao.jsonarray(text);
	}
}

