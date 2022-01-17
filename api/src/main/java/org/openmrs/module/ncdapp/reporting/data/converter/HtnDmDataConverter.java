package org.openmrs.module.ncdapp.reporting.data.converter;

import org.openmrs.Obs;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.reporting.data.converter.DataConverter;

public class HtnDmDataConverter implements DataConverter {
	
	public Integer getFlag() {
		return flag;
	}
	
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	private Integer flag;
	
	public HtnDmDataConverter() {
	}
	
	public HtnDmDataConverter(Integer flag) {
		this.flag = flag;
	}
	
	@Override
	public Object convert(Object obj) {
		String value = "";
		Obs o = (Obs) obj;
		if (o == null)
			return null;
		
		if (flag == 1 && o.getValueCoded().equals(Dictionary.getConcept("8d64fd71-8b49-4ecb-8cb8-033597fef7c1"))) {
			value = "a";
		} else if (flag == 1 && o.getValueCoded().equals(Dictionary.getConcept("e744c6e9-14c6-4cfd-92ce-19387ae87b12"))) {
			value = "b";
		} else if (flag == 2 && o.getValueCoded().equals(Dictionary.getConcept("ac58e607-21b9-4c5b-aa67-fa63ff789a12"))) {
			value = "a";
		} else if (flag == 2 && o.getValueCoded().equals(Dictionary.getConcept("0df832b1-d411-4252-9809-f8a7bde67cde"))) {
			value = "b";
		}
		return value;
	}
	
	@Override
	public Class<?> getInputDataType() {
		return String.class;
	}
	
	@Override
	public Class<?> getDataType() {
		return String.class;
	}
}
