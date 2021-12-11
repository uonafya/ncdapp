package org.openmrs.module.ncdapp.model;

public class Prescription {
	
	private String name;
	
	private String frequency;
	
	private Integer formulation;
	
	private Integer days;
	
	private String comment;
	
	private String dosage;
	
	private Integer drugUnit;
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Integer getDays() {
		return days;
	}
	
	public void setDays(Integer days) {
		this.days = days;
	}
	
	public Integer getFormulation() {
		return formulation;
	}
	
	public void setFormulation(Integer formulation) {
		this.formulation = formulation;
	}
	
	public String getFrequency() {
		return frequency;
	}
	
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDosage() {
		return dosage;
	}
	
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	
	public Integer getDrugUnit() {
		return drugUnit;
	}
	
	public void setDrugUnit(Integer drugUnit) {
		this.drugUnit = drugUnit;
	}
	
	@Override
	public String toString() {
		return "Prescription{" + "name='" + name + '\'' + ", frequency='" + frequency + '\'' + ", formulation="
		        + formulation + ", days=" + days + ", comment='" + comment + '\'' + ", dosage='" + dosage + '\''
		        + ", drugUnit=" + drugUnit + '}';
	}
}
