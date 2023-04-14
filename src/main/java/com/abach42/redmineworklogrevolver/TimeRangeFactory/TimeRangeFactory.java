package com.abach42.redmineworklogrevolver.TimeRangeFactory;

import java.util.HashMap;
import java.util.Map;

import com.abach42.redmineworklogrevolver.Exception.TimeRangeFactoryException;

/**
 * Factory-method "pattern" to create a API call ready-to-use date from-to parameter.
 */
public class TimeRangeFactory implements TimeRangeFactoryInterface {
	
	/**
	 * User options to be chosen
	 */
	public enum TimeRangeTypes {
		TODAY(1), 
        LAST_MONTH(2),
        THIS_MONTH(3),
        LAST_WEEK(4), 
        THIS_WEEK(5), 
        YESTERDAY(6);

		public final Integer inputKey;

		TimeRangeTypes(Integer inputKey) {
			this.inputKey = inputKey;
		}
    }
	
	protected static final Map<Integer, TimeRangeable> factoryTypes = new HashMap<>();
	
	static {

		/**
		 * set up for factory-method "pattern".
		 */
		factoryTypes.put(TimeRangeTypes.TODAY.inputKey, new TimeRangeTodayProduct());
		factoryTypes.put(TimeRangeTypes.LAST_MONTH.inputKey, new TimeRangeLastMonthProduct());
		factoryTypes.put(TimeRangeTypes.THIS_MONTH.inputKey, new TimeRangeThisMonthProduct());
		factoryTypes.put(TimeRangeTypes.LAST_WEEK.inputKey, new TimeRangeLastWeekProduct());
		factoryTypes.put(TimeRangeTypes.THIS_WEEK.inputKey, new TimeRangeThisWeekProduct());
		factoryTypes.put(TimeRangeTypes.YESTERDAY.inputKey, new TimeRangeYesterdayProduct());
	}
    
    @Override
	public TimeRangeable getTimeRange(Integer inputKey) throws TimeRangeFactoryException {
        if(validateInput(inputKey) == false) {
        	throw new TimeRangeFactoryException(null);
        }

		TimeRangeable product = factoryTypes.get(inputKey);
		initializeProduct(product);
		return product;
    }

	protected boolean validateInput(Integer numberInput) {
		if (numberInput == null) {
			return false;
		}
		return factoryTypes.containsKey(numberInput);
	}

	private void initializeProduct(TimeRangeable product) {
		product.setFrom();
		product.setTo();
	}
}