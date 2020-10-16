package com.addressbooksystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class AddressBookJSONService {
	private static final String STRING_READ_SAMPLE = "writeSample.csv";
	private static final String STRING_READ_SAMPLE_JSON = "writeSampleJSON.csv";

	public void writeToJson() {
		try {
			Reader reader = Files.newBufferedReader(Paths.get(STRING_READ_SAMPLE));
			CsvToBeanBuilder<contactInfo> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
			csvToBeanBuilder.withType(contactInfo.class);
			csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
			CsvToBean<contactInfo> csvToBean = csvToBeanBuilder.build();
			List<contactInfo> contact = csvToBean.parse();
			Gson gson = new Gson();
			String json  = gson.toJson(contact);
			FileWriter writer = new FileWriter(STRING_READ_SAMPLE_JSON);
			writer.write(json);
			writer.close();
			BufferedReader br = new BufferedReader(new FileReader(STRING_READ_SAMPLE_JSON));
			contactInfo[] contactNew = gson.fromJson(br,contactInfo[].class);
			List<contactInfo> list = Arrays.asList(contactNew);
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
