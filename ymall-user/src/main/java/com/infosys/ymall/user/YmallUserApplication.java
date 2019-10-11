package com.infosys.ymall.user;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.infosys.ymall.user.mapper")
public  class YmallUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(YmallUserApplication.class, args);
	}

}
