package com.example.news.ModelClasses;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MainResponse{

//	@SerializedName("totalResults")
//	private int totalResults;

	@SerializedName("articles")
	private List<ArticlesItem> articles;
//
//	@SerializedName("status")
//	private String status;
//
//	public int getTotalResults(){
//		return totalResults;
//	}

	public List<ArticlesItem> getArticles(){
		return articles;
	}

//	public String getStatus(){
//		return status;
//	}


}