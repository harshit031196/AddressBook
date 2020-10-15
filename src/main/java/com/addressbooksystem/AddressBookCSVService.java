package com.addressbooksystem;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class AddressBookCSVService {
	
	private static final String STRING_WRITE_SAMPLE = "writeSample.csv";
	private static final String STRING_READ_SAMPLE = "writeSample.csv";
	public void writeToCSV(ArrayList<contactInfo> book) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		try(Writer writer = Files.newBufferedWriter(Paths.get(STRING_WRITE_SAMPLE));){
			StatefulBeanToCsv<contactInfo> beanToCsv = new StatefulBeanToCsvBuilder(writer).withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
			beanToCsv.write(book);
		}
	}
	public List<contactInfo> readToCSV() throws IOException{
		List<contactInfo> contactList = new ArrayList<contactInfo>();
		try(Reader reader = Files.newBufferedReader(Paths.get(STRING_READ_SAMPLE));
				CSVReader csvReader = new CSVReader(reader);
				){
			String [] nextRecord;
			boolean flag=true;
			while((nextRecord=csvReader.readNext())!=null) {
				contactList.add(new contactInfo(nextRecord[0],nextRecord[1],nextRecord[2],nextRecord[3],nextRecord[4],nextRecord[5],nextRecord[6],nextRecord[7]));
			}
		}
		return contactList;
	}
	
}
