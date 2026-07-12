# Field Translate

Spring Boot 3 通用字段翻译框架。在 **不修改 DTO 类结构** 的前提下，通过注解 + 插件在 JSON 响应中 **Append** 动态翻译字段（字典、汇率等）。

- **Java 17+** / **Spring Boot 3.3+**
- 核心显式启用：`@EnableFieldTranslate`
- 插件 classpath 自动发现（`AutoConfiguration.imports`）
- 默认 Jackson Append；可 exclusion 后走 `ResponseBodyAdvice` 或自定义 `TranslateSerializationHook`

## 模块

| 模块 | 说明 |
|------|------|
| `field-translate-spring-boot-core` | SPI、Context、Aspect、缓存（无 Jackson） |
| `field-translate-spring-boot-autoconfigure` | `@EnableFieldTranslate`、核心 Bean |
| `field-translate-spring-boot-jackson` | Jackson `BeanSerializerModifier`（可选，starter 默认包含） |
| `field-translate-dict` | 字典翻译插件 |
| `field-translate-spring-boot-openapi` | OpenAPI/Swagger 文档增强（可选） |
| `field-translate-spring-boot-starter` | 开箱即用（core + autoconfigure + jackson） |

## 快速开始

### 1. 依赖

```xml
<dependency>
    <groupId>io.github.juwencheng</groupId>
    <artifactId>field-translate-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
<!-- 字典插件：加依赖即自动注册，无需额外 @Enable 注解 -->
<dependency>
    <groupId>io.github.juwencheng</groupId>
    <artifactId>field-translate-dict</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 2. 启用核心

```java
@SpringBootApplication
@EnableFieldTranslate
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 3. 使用注解

```java
@RestController
public class OrderController {

    @GetMapping("/orders/{id}")
    @TranslateResponse
    public OrderDto getOrder() {
        return new OrderDto();
    }
}

public class OrderDto {
    private String orderId = "ORDER-100";
    private Integer status = 1;

    @TranslateField(value = "statusText", translator = DictFieldTranslator.class, args = "order_status")
    private Integer status;
}
```

实现 `IDictDataProvider` Bean 提供字典数据（或使用 `DefaultDictDataProvider` 占位）。

## 依赖组合

| 场景 | 依赖 |
|------|------|
| 仅翻译基础设施 | `field-translate-spring-boot-starter` |
| + 字典 | 再加 `field-translate-dict` |
| + 汇率 | 使用 [auto-exchange](https://github.com/juwencheng/auto-exchange) 插件（Phase 3） |
| + Swagger 文档 | 再加 `field-translate-spring-boot-openapi` + `springdoc-openapi-starter-webmvc-ui` |

## Swagger / OpenAPI

引入 `field-translate-spring-boot-openapi` 后，标注了 `@TranslateResponse` 的接口会自动在 Swagger 文档中追加 **动态字段说明表**（Append 模式下 DTO 之外的翻译字段）。

```xml
<dependency>
    <groupId>io.github.juwencheng</groupId>
    <artifactId>field-translate-spring-boot-openapi</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

启动演示应用后访问 Swagger UI：

http://localhost:8088/swagger-ui/index.html

## 插件自动发现

插件 jar 通过以下文件注册自动配置：

`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

核心 **不会** 自动启动，必须 `@EnableFieldTranslate`。插件配置类需：

```java
@AutoConfigureAfter(name = "io.github.juwencheng.fieldtranslate.autoconfigure.FieldTranslateAutoConfiguration")
@ConditionalOnBean(TranslateStrategy.class)
public class DictAutoConfiguration { ... }
```

关闭插件：`field.translate.dict.enabled=false` 或不引依赖。

## 配置

```yaml
field:
  translate:
    enabled: true
    aspect-order: 2147483647
    translate-cache:
      enabled: true
    output:
      mode: jackson-hook          # 默认；可选 response-body-advice
      preferred: jackson

field.translate.dict:
  enabled: true
  cache-ttl: 1h
```

## Jackson 与输出模式

### 默认（Jackson Hook）

starter 包含 `field-translate-spring-boot-jackson`，在序列化阶段按对象 identity 追加字段，嵌套结构支持最好。

### 排除 Jackson 模块

```xml
<dependency>
    <groupId>io.github.juwencheng</groupId>
    <artifactId>field-translate-spring-boot-starter</artifactId>
    <exclusions>
        <exclusion>
            <groupId>io.github.juwencheng</groupId>
            <artifactId>field-translate-spring-boot-jackson</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

```yaml
field:
  translate:
    output:
      mode: response-body-advice
```

框架通过 `ResponseBodyAdvice` + `AppendedFieldsMerger` 将追加字段合并为 Map 树，适用于 Gson / Fastjson2 等 JSON 转换器。

## 替换序列化框架（SPI）

### 核心数据契约

```java
// AOP 阶段写入
translateContext.addAppendedData(targetObject, "statusText", "已支付");

// 输出阶段读取
Map<String, Object> extra = translateContext.getAppendedDataFor(targetObject);
```

### 层 1：框架原生钩子（推荐）

```java
@Component
public class GsonTranslateSerializationHook implements TranslateSerializationHook {
    @Override
    public String getId() { return "gson"; }

    @Override
    public boolean isAvailable() { ... }

    @Override
    public void register(SerializationHookContext context) {
        // 向 GsonBuilder 注册 TypeAdapterFactory，序列化时读取 TranslateContext
    }
}
```

### 层 2：自定义 ResponseBodyEnhancer

```java
@Bean
TranslateResponseBodyEnhancer myEnhancer(AppendedFieldsMerger merger) {
    return new MyEnhancer(merger);
}
```

## 开发自定义翻译插件

1. 实现 `FieldTranslator`，注册为 `@Component`
2. 可选：实现 `TranslateContextContributor` 写入请求级 Context
3. 可选：实现 `TranslateCacheStrategy` + `TranslatorCacheBinding` Bean

```java
@Bean
TranslatorCacheBinding myCacheBinding() {
    return new TranslatorCacheBinding(MyTranslator.class, MyCacheStrategy.class);
}
```

## 本地构建与演示

```bash
mvn install -DskipTests
cd field-translate-spring-boot-test-app
mvn spring-boot:run
curl http://localhost:8088/test/orderWithDict
```

## 与 auto-exchange 的关系

本仓库为 **通用翻译核心**。汇率转换由 [auto-exchange](https://github.com/juwencheng/auto-exchange) 作为 `ExchangeFieldTranslator` 插件提供（迁移进行中）。

## 许可证

MIT
