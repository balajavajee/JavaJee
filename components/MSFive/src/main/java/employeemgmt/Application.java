package com.yoma.adminportal.employeemgmt;

import com.backbase.buildingblocks.backend.configuration.autoconfigure.BackbaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The main Application class for Persistence service.
 */
@BackbaseApplication
@EnableAutoConfiguration
@EnableHystrix
@EnableJpaRepositories(basePackages = {"com.backbase.buildingblocks.persistence.repository",
    "com.yoma.adminportal.employeemgmt.persistence.repository"})
@EntityScan(basePackages = {"com.backbase.buildingblocks.persistence.model",
    "com.yoma.adminportal.employeemgmt.command.domain",
    "com.yoma.adminportal.employeemgmt.persistence.model"})
@ComponentScan(basePackages = {"com.backbase", "com.yoma.adminportal.employeemgmt",
    "com.yoma.adminportal.utilities", "com.yoma.auditservice.publisher"},
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)})
public class Application extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
