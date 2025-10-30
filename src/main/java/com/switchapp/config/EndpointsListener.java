package com.switchapp.config;

import com.switchapp.model.RestApis;
import com.switchapp.repository.RestApisRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

@Component
public class EndpointsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointsListener.class);

    @Autowired
    RestApisRepository restApiDetailsRepository;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        Map<RequestMappingInfo, HandlerMethod> methods = applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class)
                .getHandlerMethods();

        String urlPatternsPath = "", httpMethodsName = "", returnType = "";
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : methods.entrySet()) {
            RestApis apiDetails = new RestApis();
            RequestMappingInfo key = entry.getKey();
            LOGGER.info("key....:" + key);
            HandlerMethod value = entry.getValue();
            StringBuffer buf = new StringBuffer();
            buf.append((key.getName() != null) ? "Name :" + key.getName() + "\n" : "");
            buf.append((key.getMethodsCondition() != null) ? "MethodsCondition :" + key.getMethodsCondition() + "\n" : "");
            buf.append((key.getProducesCondition() != null) ? "ProducesCondition :" + key.getProducesCondition() + "\n" : "");
            buf.append((key.getConsumesCondition() != null) ? "ConsumesCondition :" + key.getConsumesCondition() + "\n" : "");
            buf.append((key.getHeadersCondition() != null) ? "HeadersCondition :" + key.getHeadersCondition() + "\n" : "");
            buf.append((key.getCustomCondition() != null) ? "CustomCondition :" + key.getCustomCondition() + "\n" : "");
            buf.append((key.getParamsCondition() != null) ? "ParamsCondition :" + key.getParamsCondition() + "\n" : "");
            LOGGER.info("PatternsCondition :" + buf);
            LOGGER.info("Test...:" + key.getPathPatternsCondition().toString());

            urlPatternsPath = key.getPathPatternsCondition().toString().trim().replaceAll("\\[|\\]", "");
            apiDetails.setUrlPatternsPath(urlPatternsPath);
            httpMethodsName = key.getMethodsCondition().toString().trim().replaceAll("\\[|\\]", "");
            apiDetails.setHttpMethodsName(httpMethodsName);
            apiDetails.setApiConsumes(key.getConsumesCondition().toString().trim().replaceAll("\\[|\\]", ""));
            buf.append((key.getPathPatternsCondition() != null) ? "PatternsCondition :" + key.getPathPatternsCondition() + "\n" : "");
            if (value != null) {
                buf.append((value.getMethod() != null) ? "Method :" + value.getMethod() + "\n" : "");
                buf.append((value.getBean() != null) ? "Bean :" + value.getBean() + "\n" : "");
                buf.append((value.getShortLogMessage() != null) ? "ShortLogMessage :" + value.getShortLogMessage() + "\n" : "");
                buf.append((value.getResolvedFromHandlerMethod() != null) ? "ResolvedFromHandlerMethod :" + value.getResolvedFromHandlerMethod() + "\n" : "");
                buf.append((value.getBeanType() != null) ? "BeanType :" + value.getBeanType() + "\n" : "");
                buf.append((value.getMethodParameters() != null) ? "MethodParameters :" + value.getMethodParameters() + "\n" : "");
                buf.append((value.getReturnType() != null) ? "ReturnType :" + value.getReturnType() + "\n" : "");
                apiDetails.setJavaClassName(value.getBeanType().getName());
                returnType = value.getMethod().toString().trim().replaceAll("^[A-z]{1,}\\s{1}", "").replaceAll("\\s{1}.*", "");
                apiDetails.setReturnType(returnType);
                apiDetails.setMethodParamsSign(value.getMethod().toString().trim().replaceAll("[A-z]{1,}\\s{1}", ""));
            }
            LOGGER.info(apiDetails.toString());
            try {
                RestApis existingRestApiDetails = null;
                List<RestApis> listOfRestApiDetails = restApiDetailsRepository.findByUrlPatternsPathAndHttpMethodsName(urlPatternsPath, httpMethodsName);
                int countRestApi = listOfRestApiDetails.size();
                if (listOfRestApiDetails != null && countRestApi > 1) {
                    listOfRestApiDetails = restApiDetailsRepository.findByUrlPatternsPathAndHttpMethodsNameAndReturnType(urlPatternsPath, httpMethodsName, returnType);
                    countRestApi = listOfRestApiDetails.size();
                    if (countRestApi == 1 && listOfRestApiDetails.get(0) != null) {
                        existingRestApiDetails = listOfRestApiDetails.get(0);
                    }
                } else if (countRestApi == 1 && listOfRestApiDetails.get(0) != null) {
                    existingRestApiDetails = listOfRestApiDetails.get(0);
                }
                if (existingRestApiDetails != null && StringUtils.isNotBlank(existingRestApiDetails.getUrlPatternsPath()) && StringUtils.isNotBlank(existingRestApiDetails.getHttpMethodsName())) {
                    if (getCompareNewVsExisting(existingRestApiDetails.getApiConsumes(), apiDetails.getApiConsumes()))
                        existingRestApiDetails.setApiConsumes(apiDetails.getApiConsumes());
                    if (getCompareNewVsExisting(existingRestApiDetails.getJavaClassName(), apiDetails.getJavaClassName()))
                        existingRestApiDetails.setJavaClassName(apiDetails.getJavaClassName());
                    if (getCompareNewVsExisting(existingRestApiDetails.getReturnType(), apiDetails.getReturnType()))
                        existingRestApiDetails.setReturnType(apiDetails.getReturnType());
                    if (getCompareNewVsExisting(existingRestApiDetails.getMethodParamsSign(), apiDetails.getMethodParamsSign()))
                        existingRestApiDetails.setMethodParamsSign(apiDetails.getMethodParamsSign());
                    restApiDetailsRepository.save(existingRestApiDetails);
                } else if (existingRestApiDetails == null && apiDetails != null) {
                    restApiDetailsRepository.save(apiDetails);
                }
            } catch (Exception e) {
                LOGGER.info("urlPatternsPathurlPatternsPathurlPatternsPathurlPatternsPath :" + urlPatternsPath);
                e.printStackTrace();
            }

            LOGGER.info(buf.toString());
        }
    }

    private boolean getCompareNewVsExisting(String existing, String newOne) {
        return !existing.equals(newOne);
    }

    class ApiDetails {
        private String urlPatternsPath;
        private String httpMethodsName;
        private String apiConsumes;
        private String javaClassName;
        private String returnType;
        private String methodParamsSign;

        public String getUrlPatternsPath() {
            return urlPatternsPath;
        }

        public void setUrlPatternsPath(String urlPatternsPath) {
            this.urlPatternsPath = urlPatternsPath;
        }

        public String getHttpMethodsName() {
            return httpMethodsName;
        }

        public void setHttpMethodsName(String httpMethodsName) {
            this.httpMethodsName = httpMethodsName;
        }

        public String getApiConsumes() {
            return apiConsumes;
        }

        public void setApiConsumes(String apiConsumes) {
            this.apiConsumes = apiConsumes;
        }

        public String getJavaClassName() {
            return javaClassName;
        }

        public void setJavaClassName(String javaClassName) {
            this.javaClassName = javaClassName;
        }

        public String getReturnType() {
            return returnType;
        }

        public void setReturnType(String returnType) {
            this.returnType = returnType;
        }

        public String getMethodParamsSign() {
            return methodParamsSign;
        }

        public void setMethodParamsSign(String methodParamsSign) {
            this.methodParamsSign = methodParamsSign;
        }

        @Override
        public String toString() {
            return "ApiDetails{" +
                    "urlPatternsPath='" + urlPatternsPath + '\'' +
                    ", httpMethodsName='" + httpMethodsName + '\'' +
                    ", apiConsumes='" + apiConsumes + '\'' +
                    ", javaClassName='" + javaClassName + '\'' +
                    ", returnType='" + returnType + '\'' +
                    ", methodParamsSign='" + methodParamsSign + '\'' +
                    '}';
        }
    }
}
