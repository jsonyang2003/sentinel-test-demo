package com.json.sentinel.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.json.sentinel.common.Result;
import com.json.sentinel.config.ApolloListener;
import com.json.sentinel.dto.Product;
import com.json.sentinel.service.ProductService;


@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ApolloListener apolloListener;
	
	
	private Logger logger = LoggerFactory.getLogger(ProductController.class);
	/**
	 * get("http://localhost:9000/product/add?id=1009&name=iphonese&desc=redandblack")
	 * 
	 */
	@GetMapping(value = "/save")
	public Result save(@RequestParam Long id, @RequestParam String name, @RequestParam String desc) {
		logger.error("add :{}", id);
		Product product = new Product(id, name, desc);
		return productService.add(product);
	}

	@GetMapping(value = "/{id}")
	public Result get(@PathVariable Long id) {
		logger.error("get with id:{}", id);
		return productService.get(id);
	}

	@GetMapping(value = "/get")
	public Result getById(@RequestParam Long id) {
		logger.error("get with id:{}", id);
		return productService.query(id);
	}

	@GetMapping(value = "/search")
	public Result search(@RequestParam String name) {
		logger.error("get with name:{}", name);
		return productService.search(name);
	}

	@PostMapping(value = "add")
	public Result add(@RequestBody Product product) {
		logger.error("saveproduct:{}", JSON.toJSON(product));
		return productService.add(product);
	}
	
	@GetMapping(value = "/uprule")
	public Result uprule(@RequestParam String resource,@RequestParam Integer count) {
		logger.error("uprule resource:{},count {}", resource,count);
		apolloListener.initFlowRules(resource, count);
		return Result.success("uprule done" );
	}

}
