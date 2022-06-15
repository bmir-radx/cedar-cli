package org.metadatacenter.csvpipeline;

import org.metadatacenter.csvpipeline.redcap.DataDictionaryParser;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CsvPipelineApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(CsvPipelineApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		System.out.println(args.getOptionValues(""));
	}

	@Bean
	public DataDictionaryParser redCapCsvParser() {
		return new DataDictionaryParser();
	}
}
