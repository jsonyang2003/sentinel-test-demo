package com.json.sentinel.service;

import org.springframework.stereotype.Service;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.json.sentinel.common.ExceptionUtil;
import com.json.sentinel.common.Result;
import com.json.sentinel.dto.Product;

@Service
public class ProductServiceV2 {

	public Result add(Product product) {
		return Result.success("done");
	}

	public Result<Product> get(Long id) {
		Product product = new Product(id, "戴尔DELL服务器电源 12V 495W/750W/1100W/R620/720/82冗余热插拔电源_id" + id,
				"产品名称: Dell/戴尔 1100W电源品牌: Dell/戴尔型号: 1100W电源成色: 全新售后服务: 全国联保产地: 其他海外地区颜色分类: DELL 495W DELL 750W DELL 1100W 台达 550W 台达 750W 长城 730W额定功率: 750W毛重: 12KG是否支持多路12V: 支持显卡12V接口数量: 0个D型接口数量: 2个SATA口数量: 2个80 PLUS认证: 金牌CPU供电接口: 4pin适用对象: 服务器是否支持模块化: 支持PFC类型: 主动式包装体积: 12K"
						+ id);
		return Result.success(product);
	}

	public Result<Product> query(Long id) {
		Product product = new Product(id, "戴尔DELL服务器电源 12V 495W/750W/1100W/R620/720/82冗余热插拔电源_id" + id,
				"产品名称: Dell/戴尔 1100W电源品牌: Dell/戴尔型号: 1100W电源成色: 全新售后服务: 全国联保产地: 其他海外地区颜色分类: DELL 495W DELL 750W DELL 1100W 台达 550W 台达 750W 长城 730W额定功率: 750W毛重: 12KG是否支持多路12V: 支持显卡12V接口数量: 0个D型接口数量: 2个SATA口数量: 2个80 PLUS认证: 金牌CPU供电接口: 4pin适用对象: 服务器是否支持模块化: 支持PFC类型: 主动式包装体积: 12K"
						+ id);
		return Result.success(product);
	}

	public Result search(String name) {
		if (name == null || "bad".equals(name)) {
			throw new IllegalArgumentException("oops");
		}
		if ("foo".equals(name)) {
			throw new IllegalStateException("oops");
		}
		return Result.success("find result : " + name);
	}

}
