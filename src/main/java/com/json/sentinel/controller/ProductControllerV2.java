package com.json.sentinel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.json.sentinel.common.Result;
import com.json.sentinel.dto.Product;
import com.json.sentinel.service.ProductService;
import com.json.sentinel.service.ProductServiceV2;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product2")
@Slf4j
public class ProductControllerV2 {

	@Autowired
	private ProductServiceV2 productServiceV2;

	/**
	 * get("http://localhost:9000/product/add?id=1009&name=iphonese&desc=redandblack")
	 * 
	 */
	@GetMapping(value = "/save")
	public Result save(@RequestParam Long id, @RequestParam String name, @RequestParam String desc) {
		log.error("add :{}", id);
		Product product = new Product(id, name, desc);
		return productServiceV2.add(product);
	}

	@GetMapping(value = "/{id}")
	public Result get(@PathVariable Long id) {
		log.error("get with id:{}", id);
		return productServiceV2.get(id);
	}

	@GetMapping(value = "/get")
	public Result getById(@RequestParam Long id) {
		log.error("get with id:{}", id);
		return productServiceV2.query(id);
	}

	@GetMapping(value = "/search")
	public Result search(@RequestParam String name) {
		log.error("get with name:{}", name);
		return productServiceV2.search(name);
	}

	@PostMapping(value = "add")
	public Result add(@RequestBody Product product) {
		log.error("saveproduct:{}", JSON.toJSON(product));
		return productServiceV2.add(product);
	}
}
