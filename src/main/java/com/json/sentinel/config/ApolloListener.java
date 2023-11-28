package com.json.sentinel.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
//import com.ctrip.framework.apollo.Config;
//import com.ctrip.framework.apollo.ConfigService;
//import com.ctrip.framework.apollo.model.ConfigChange;
//import com.ctrip.framework.apollo.model.ConfigChangeEvent;
//import com.ctrip.framework.apollo.ConfigChangeListener;

@Service
public class ApolloListener {

	static final String ruleKey = "sentinel_limit_xxapi_key"; // 自定义

	static final int limitCount = 900;

	@PostConstruct
	public void init() {
		initFlowRules("getProduct", 1);

//		Config config = ConfigService.getAppConfig(); // config instance is singleton for each namespace and is never //
//														// null
//		config.addChangeListener(new ConfigChangeListener() {
//			@Override
//			public void onChange(ConfigChangeEvent changeEvent) {
//				System.out.println("Changes for namespace " + changeEvent.getNamespace());
//				for (String key : changeEvent.changedKeys()) {
//					if (ruleKey.equals(key)) {
//						ConfigChange change = changeEvent.getChange(key);
//						Integer value = Integer.parseInt(change.getNewValue());
//						initFlowRules("getProduct", value);
//					}
//
//				}
//			}
//		});
	}

	public void initFlowRules(String resource, int limitCount) {
		List<FlowRule> rules = new ArrayList<>();
		FlowRule rule = new FlowRule();
		rule.setResource(resource);
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS).setCount(limitCount);
		rules.add(rule);
		FlowRuleManager.loadRules(rules);
	}

	public void updateFlowRules(String resource, int limitCount) {
		List<FlowRule> rules = FlowRuleManager.getRules();
		Map<String, FlowRule> ruleMap = new HashMap<>();
		for (FlowRule item : rules) {
			String resourceName = item.getResource();
			ruleMap.computeIfAbsent(resourceName, e -> {
				return item;
			});
		}
		FlowRule rule = new FlowRule();
		rule.setResource(resource);
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS).setCount(limitCount);
		//覆盖更新
		ruleMap.put(resource, rule);
		FlowRuleManager.loadRules(new ArrayList<FlowRule>( ruleMap.values()));
	}

}
