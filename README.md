# sentinel-test-demo
Sentinel 
1.不依赖任何框架/库，能够运行于 Java 8 及以上的版本的运行时环境，对Spring 生态框架也有较好的支持
2.应用限流和降级
   限流 FlowRule
Field	说明	默认值
resource	资源名，资源名是限流规则的作用对象	
count	限流阈值	
grade	限流阈值类型，QPS 或线程数模式	QPS 模式
limitApp	流控针对的调用来源	default，代表不区分调用来源
strategy	调用关系限流策略：直接、链路、关联	根据资源本身（直接）
controlBehavior	流控效果（直接拒绝 / 排队等待 / 慢启动模式），不支持按调用关系限流	直接拒绝
同一个资源可以同时有多个限流规则。
limitApp的值  需要在请求入口处根据来源手动统一设置, 如 HTTP 入口ContextUtil.enter(resourceName, origin)

熔断降级 DegradeRule  
(慢调用比例 (SLOW_REQUEST_RATIO) 异常比例 (ERROR_RATIO)异常数 (ERROR_COUNT))
Field	说明	默认值
resource	资源名，即规则的作用对象	
grade	熔断策略，支持慢调用比例/异常比例/异常数策略	慢调用比例
count	慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用）；异常比例/异常数模式下为对应的阈值	
timeWindow	熔断时长，单位为 s	
minRequestAmount	熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断（1.7.0 引入）	5
statIntervalMs	统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）	1000 ms
slowRatioThreshold	慢调用比例阈值，仅慢调用比例模式有效（1.8.0 引入）	
同一个资源可以同时有多个降级规则。

3.网关限流和降级 支持zuul 和 gateway (GlobalFilter  --> 支持routeId 和 定义path集)

4.业务使用示例: 
定义规则，这一步可以在控制台中界面操作
private static void initFlowRules(){
    List<FlowRule> rules = new ArrayList<>();
    FlowRule rule = new FlowRule();
    rule.setResource("web_login");
    rule.setGrade(RuleConstant.FLOW_GRADE_QPS);//FLOW_GRADE_THREAD
    // Set limit QPS to 20.
    rule.setCount(20);
    rules.add(rule);
    //全局加载规则
    FlowRuleManager.loadRules(rules);
}

 // 原函数  
    @SentinelResource(value = "test", blockHandler = "exceptionHandler", fallback = "testFallback")
    public String test(long s) {
        return String.format("Hello at %d", s);
    }
     blockHandler fallback 非必须

    // Fallback 函数，函数签名与原函数一致或加一个 Throwable 类型的参数.
    public String testFallback(long s) {
        return String.format("Halooooo %d", s);
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public String exceptionHandler(long s, BlockException ex) {
        // Do some log here.
        ex.printStackTrace();
        return "Oops, error occurred at " + s;
    }



curl http://localhost:8719/cnode?id=resourceName
结果示例：

idx id                                thread    pass      blocked   success    total    aRt   1m-pass   1m-block   1m-all   exeption   
6   /app/aliswitch2/machines.json     0         0         0         0          0        0     0         0          0        0          
7   /app/sentinel-admin/machines.json 0         1         0         1          1        6     0         0          0        0          
8   /identity/machine.json            0         0         0         0          0        0     0         0          0        0          
9   /registry/machine                 0         2         0         2          2        1     192       0          192      0          
10  /app/views/machine.html           0         1         0         1          1        2     0   


curl http://localhost:8719/clusterNode
[
 {"avgRt":0.0, //平均响应时间
 "blockRequest":0, //每分钟拦截的请求个数
 "blockedQps":0.0, //每秒拦截个数
 "curThreadNum":0, //并发个数
 "passQps":1.0, // 每秒成功通过请求
 "passReqQps":1.0, //每秒到来的请求
 "resourceName":"/registry/machine", 资源名称
 "timeStamp":1529905824134, //时间戳
 "totalQps":1.0, // 每分钟请求数
 "totalRequest":193}, 
  ....
]

查看应用当前规则:

http://localhost:8719/getRules?type=flow
http://localhost:8719/getRules?type=degrade
http://localhost:8719/getRules?type=system
//热点规则
http://localhost:8719/getParamRules


统计日志
所有的统计信息都存储在本地文件系统上
${home}/logs/csp/${appName}-${pid}-metrics.log.${date}.xx。例如，该日志的名字可能为 app-3518-metrics.log.2023-11-10.1
1529573107000|2023-11-10 07:25:07|methodA(java.lang.String,long)|10|3601|10|0|2


5.控制台
    a.不支持上千台的机器节点，全部数据为主动拉取各个应用的本地机器数据到本机内存，只存放聚合5分钟内的数据，无法查看全局的历史数据.  聚合能力有限。
    b.如何应用没有初始化规则数据，只通过控制台下发规则，那么应用重启后规则就会实效，



最佳实践
方案1  改造规则推送，推送时，将规则直接推送至配置中心，应用监听配置中心的指定规则的改动，由配置中心下发配置到应用，从而将配置固化存储. 目前可支持nacos apollo配置中心，须对Dashboard做少量改造
方案2  将规则配置与配置中心关联，初期须配置全部须流控的资源规则配置，不许要Dashboard,Dashboard可作为简单查看的实时统计信息
方案3  改造Dashboard，将拉取的统计监控数据存db.  Dashboard拉取时写db，读取时读db. 可实现查看历史的流控统计信息

权限控制
  Dashboard目前只有一个登陆身份，可以控制全局流控配置，随意更改会对系统产生风险

go语言支持，因为Sentinel的实现逻辑在应用端的进程中，go有单独的库支持，并且可以共用Dashboard
