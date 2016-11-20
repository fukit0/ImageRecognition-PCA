package ytu.ml.pca;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import user.furkan.util.ListUtils;
import user.furkan.util.StringUtils;

/**
 * Class for responsible for holding the samples seperated for traing and valitation.
 * @author furkan
 *
 */
public class DataTable {

	Logger logger = Logger.getGlobal();
	
	private Map<Integer,List<SampleObject>> trainSamples;
	private List<SampleObject> validationSamples;
	private List<SampleObject> allSamples;
	
	public DataTable() {
		trainSamples = new HashMap<>();
		validationSamples = new ArrayList<>();
		allSamples = new ArrayList<>();
	}

	
	public List<SampleObject> getAllSamples() {
		return allSamples;
	}


	public void setAllSamples(List<SampleObject> allSamples) {
		this.allSamples = allSamples;
	}


	public Map<Integer, List<SampleObject>> getTrainSamples() {
		return trainSamples;
	}

	public void setTrainSamples(Map<Integer, List<SampleObject>> trainSamples) {
		this.trainSamples = trainSamples;
	}

	public List<SampleObject> getValidationSamples() {
		return validationSamples;
	}

	public void setValidationSamples(List<SampleObject> validationSamples) {
		this.validationSamples = validationSamples;
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		for (SampleObject sample : allSamples) {
			
			for (String s : sample.getSampleValues()) {
				System.out.print(s+",");
			}
			System.out.print(sample.getClassifierNumberStr() + "\n");
		}
		
		return sb.toString();
	}
	
	public void loadDatFileToDataTable(String fileName) throws Exception{
		
		int sampleId = 0;
		
		Scanner s = new Scanner(new File(fileName));

		logger.info("Started to parsing file..");
		while (s.hasNext()) {

			String line = s.nextLine().trim();

			// if it is not a empty line
			// StringUtils is a library defined by user @furkan
			if(!StringUtils.isEmpty(line)){
				
				try {
					SampleObject sample = new SampleObject();
					sample.setLineId(sampleId);
					sample.setSampleValues(new ArrayList<>());
					
					Scanner valueScanner = new Scanner(line);
					valueScanner.useDelimiter(","); // separate values by using ','
					
					while(valueScanner.hasNext()){
						
						String pixelValue = valueScanner.next().trim();
						
						if(!StringUtils.isEmpty(pixelValue)){
							
							sample.getSampleValues().add(pixelValue);
						}
					}
					
					// remove the classifier from sample
					String classLabel = sample.getSampleValues().remove(sample.getSampleValues().size()-1);
					sample.setClassifierNumberStr(classLabel.toLowerCase());
					sample.setClassifierNumber(Integer.parseInt(classLabel));
					
					// add sample into appropriate set
					addSampleIntoAppropriateSet(sample);
					allSamples.add(sample);
					
				} catch (Exception e) {
					logger.info("Erroc occured while parsing file: " + line + "\n" + e.toString() +  "///// " + e.getMessage());
				}
			}
			
			sampleId++;
		}
	}

	private void addSampleIntoAppropriateSet(SampleObject sample) {
		
		// If this is the first sample of the class
		if(trainSamples.get(sample.getClassifierNumber()) == null){
			List<SampleObject> samples = new ArrayList<>();
			samples.add(sample);
			trainSamples.put(sample.getClassifierNumber(),samples);
		
		}
		// If there are not 5 examples yet in train set
		else if(trainSamples.get(sample.getClassifierNumber()).size()<5){
			trainSamples.get(sample.getClassifierNumber()).add(sample);
		
		}
		// add into train set
		else{
			validationSamples.add(sample);
		}
	}
	
	
}