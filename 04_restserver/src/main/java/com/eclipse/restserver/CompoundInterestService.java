package com.eclipse.restserver;

import com.eclipse.restserver.bean.CompoundBean;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains the web service methods. You can have multiple request
 * methods of the same type each in its own class or different types of requests
 * in this file.
 *
 * @author Ken Fogel
 */
// This is the path to the service method. It follows the Application path in 
// the URL as in http://localhost:8080/04_RestServer/services/compound
@Path("compound")
public class CompoundInterestService {

    // Whether you use it or not, consider always declaring a logger.
    private static final Logger LOG = Logger.getLogger(CompoundInterestService.class.
                    getName());

    // Keep the 'result' to 2 decimal places
    private final NumberFormat numberFormat;

    /**
     * Initialize and configure the numberFormat object
     */
    public CompoundInterestService() {
        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
    }

    /**
     * This method can receive the value from a query string.The annotation
     * {@literal @}QueryParam("principle") indicates that the query string is
     * expected to have a "?principle=" as in
     * http://localhost:8080/04_RestServer/services/compound?principle=100.00&annualInterestRate=0.05&compoundPerTimeUnit=12.0&time=5.0
     * You can use the line above minus the asterisk in a web browser. If you
     * are using curl the text beginning with http until the URL must be in
     * quotation marks. This is necessary due to the decimal points.
     * curl "http://localhost:8080/04_RestServer/services/compound?principle=100.00&annualInterestRate=0.05&compoundPerTimeUnit=12.0&time=5.0"
     *
     * @param principal
     * @param annualInterestRate
     * @param compoundPerTimeUnit
     * @param time
     * @return The string to return to the caller
     */
    // Request method type
    @GET
    public String sayCompoundInterest(@QueryParam("principal") double principal,
            @QueryParam("annualInterestRate") double annualInterestRate,
            @QueryParam("compoundPerTimeUnit") double compoundPerTimeUnit,
            @QueryParam("time") double time) {
        var compoundBean = new CompoundBean(principal, annualInterestRate,
                compoundPerTimeUnit, time);
        calculateCompoundInterest(compoundBean);

        return "Compound Interest:\n" + compoundBean.toString();
    }
    

    /**
     * This method is a POST service. It expects to receive an object of type
     * CompoundBean and then returns that bean after the calculation and the
     * result is added to the bean. As this method is annotated with a REST
     * annotation, @POST, the conversion from JSON to Object and then Object to
     * JSON is performed automatically.
     *
     * @param compoundBean a CompoundBean object received as JSON and converted to an
     * object
     * @return the JSON serialization of the CompoundBean object
     */
    // Request method type
    @POST
    public CompoundBean postCompoundInterest(CompoundBean compoundBean) {
        LOG.log(Level.INFO, "Input: {0}", compoundBean);
        calculateCompoundInterest(compoundBean);
        return compoundBean;
    }

    /**
     * Here is the method that calculates the result. If any of the fields in
     * the CompoundBean are out of range then the 'result' field is set to
     * 'Invalid'.
     *
     * @param compoundBean The data to be used in the calculation
     */
    public void calculateCompoundInterest(CompoundBean compoundBean) {
        if (validateBean(compoundBean)) {
            double ans = compoundBean.getPrincipal()
                    * Math.pow(1 + compoundBean.getAnnualInterestRate()
                            / compoundBean.getCompoundPerTimeUnit(),
                            compoundBean.
                                    getTime() * compoundBean.
                                    getCompoundPerTimeUnit());
            compoundBean.setResult(numberFormat.format(ans));
        } else {
            compoundBean.setResult("xxxxx");
        }
    }

    /**
     * Here we validate the four fields in the CompoundBean.
     *
     * @param compoundBean The data to be validated
     * @return Were all the fields in the CompoundBean valid? true or false
     */
    private boolean validateBean(CompoundBean compoundBean) {
        boolean valid = true;
        if (compoundBean.getPrincipal() <= 0 || compoundBean.
                getAnnualInterestRate() <= 0 || compoundBean.
                        getAnnualInterestRate() >= 1 || compoundBean.getTime() <= 0 || compoundBean.
                getCompoundPerTimeUnit() <= 0) {
            valid = false;
        }
        return valid;
    }
}
