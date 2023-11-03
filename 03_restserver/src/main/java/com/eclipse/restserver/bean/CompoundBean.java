package com.eclipse.restserver.bean;

import jakarta.enterprise.context.RequestScoped;
import java.util.logging.Logger;

/**
 * Here is the Java Bean for the Compound Interest. The 'result' field is a
 * String so that if any of the input values are out of range, as determined in
 * the CompoundInterest class, then the word 'Invalid' can be returned. There
 * are better ways to do this and using exceptions has probably crossed your
 * mind. As our goal is to create a web service, you need to consider how you
 * will inform a caller to the service that something is wrong. I leave this as
 * an exercise you can work on after the workshop.
 * 
 * If you feel that a Record is a better choice, feel free to use one but
 * remember that input should arrive as a Record and so an additional variable
 * will be needed outside the record to indicate the result.
 *
 * @author Ken Fogel
 */
@RequestScoped
public class CompoundBean {

	private double principal;
	private double annualInterestRate;
	private double compoundPerTimeUnit;
	private double time;
	private String result;

	// Whether you use it or not, consider always declaring a logger.
	private static final Logger LOG = Logger.getLogger(CompoundBean.class.getName());
        
        /**
         * To support the automatic conversion to and from JSON your class must 
         * have a default constructor
         */
        public CompoundBean() {
            result = "invalid";
        }

	public CompoundBean(double principal, double annualInterestRate, double compoundPerTimeUnit, double time) {
		this.principal = principal;
		this.annualInterestRate = annualInterestRate;
		this.compoundPerTimeUnit = compoundPerTimeUnit;
		this.time = time;
		this.result = "Invalid";
	}

	public double getPrincipal() {
		return principal;
	}

	public void setPrincipal(double principal) {
		this.principal = principal;
	}

	public double getAnnualInterestRate() {
		return annualInterestRate;
	}

	public void setAnnualInterestRate(double annualInterestRate) {
		this.annualInterestRate = annualInterestRate;
	}

	public double getCompoundPerTimeUnit() {
		return compoundPerTimeUnit;
	}

	public void setCompoundPerTimeUnit(double compoundPerTimeUnit) {
		this.compoundPerTimeUnit = compoundPerTimeUnit;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CompoundBean{");
		sb.append("principal=").append(principal);
		sb.append(", annualInterestRate=").append(annualInterestRate);
		sb.append(", compoundPerTimeUnit=").append(compoundPerTimeUnit);
		sb.append(", time=").append(time);
		sb.append(", result=").append(result);
		sb.append('}');
		return sb.toString();
	}
}
