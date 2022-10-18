package org.metadatacenter.cedar;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.metadatacenter.cedar.cli.CedarCli;
import org.metadatacenter.cedar.csv.LanguageCode;
import org.metadatacenter.cedar.csv.LanguageCodesParser;
import org.metadatacenter.cedar.ont.KnowledgeArtifactGenerator;
import org.metadatacenter.cedar.ont.VocabularyWriter;
import org.metadatacenter.cedar.redcap.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class CedarCliApplication implements ApplicationRunner, ExitCodeGenerator {

	private String noHeader = "";

	private String outputDirectory = "";

	@Autowired
	private ApplicationContext applicationContext;

	private int exitCode;

	public CedarCliApplication() {
	}

	public static void main(String[] args) {
		SpringApplication.run(CedarCliApplication.class, args);
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {
		var cli = applicationContext.getBean(CedarCli.class);
		exitCode = cli.run(args.getSourceArgs());
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}

	@Bean
	Header header() {
		if(noHeader != null) {
			return Header.WITHOUT_HEADER;
		}
		else {
			return Header.WITH_HEADER;
		}
	}

	@Bean
	public DataDictionaryParser redCapCsvParser() {
		return new DataDictionaryParser();
	}

	@Bean
	DataDictionaryProcessor dataDictionaryProcessor(DataDictionaryParser dataDictionaryParser,
													Header header,
													DataDictionaryRowProcessor valuesProcessor) {
		return new DataDictionaryProcessor(header, dataDictionaryParser, valuesProcessor);
	}

	@Bean
	VocabularyWriter vocabularyWriter(KnowledgeArtifactGenerator knowledgeArtifactGenerator) {
		return new VocabularyWriter(knowledgeArtifactGenerator, Path.of(outputDirectory));
	}

	@Bean
	DataDictionaryRowProcessor dataDictionaryValuesProcessor(KnowledgeArtifactGenerator knowledgeArtifactGenerator,
															 TemplateFieldWriter templateFieldWriter,
															 VocabularyWriter vocabularyWriter) {
		return new DataDictionaryRowProcessor(knowledgeArtifactGenerator,
											  vocabularyWriter,
											  templateFieldWriter, outputDirectory);
	}

	@Bean
	Module javaDateTimeModule() {
		return new JavaTimeModule();
	}

	@Bean
	LanguageCodesParser languageCodesParser() {
		return new LanguageCodesParser();
	}

	@Bean
	List<LanguageCode> languageCodes(LanguageCodesParser parser) throws IOException {
		var file = ResourceUtils.getFile("classpath:lang-tags.csv");
		var codes = Files.readString(file.toPath());
		return parser.parse(codes);
	}
}
