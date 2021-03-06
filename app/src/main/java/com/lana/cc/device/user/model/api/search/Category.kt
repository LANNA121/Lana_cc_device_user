package com.lana.cc.device.user.model.api.search

import com.squareup.moshi.JsonClass

data class Category(
	val id: Int,
	val color: String,
	val bgColor: String,
	val img: String,
	val name: String,
	val content: String,
	val description: String,
	val action: List<String>
){
	companion object{
		fun getNull(): Category = Category(
			-1,
			"",
			"",
			"",
			"未查询到分类",
			"",
			"",
			listOf()
			
		)
	}

	
}