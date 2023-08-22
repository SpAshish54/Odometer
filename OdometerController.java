package controller;

import controller.OdometerController;

public class OdometerController {
	private static final String DIGITS = "123456789";

	  private int reading;
	  private static int getMinReading(int size) {
		    return Integer.valueOf(DIGITS.substring(0, size));
		  }

		  private static int getMaxReading(int size) {
		    return Integer.valueOf(DIGITS.substring(DIGITS.length() - size, DIGITS.length()));
		  }

		  private static int getSize(int reading) {
		    return String.valueOf(reading).length();
		  }
		  
		  public int getSize() {
		    return getSize(this.reading);
		  }
		  public OdometerController(int newReading) {
			  reading = newReading;
		  }
		  
		  public int getCurrentReading() {
		    return reading;
		  }
		  public void setReading(int reading){
			    this.reading = reading;
		  }
		  public static boolean isAscending(int reading) {
			    if (reading < 10) {
			      return true;
			    }
			    if (reading % 10 <= (reading / 10) % 10) {
			      return false;
			    }
			    return isAscending(reading / 10);
			  }

		  public int getNextReading() {
			  do {
			      if (reading == getMaxReading(getSize(reading))) {
			        reading = getMinReading(getSize(reading));
			      } else {
			        reading++;
			      }
			    } while (!isAscending(reading));
			  return reading;
		  }

		  public int getPreviousReading() {
			    do {
			      if (reading == getMinReading(getSize(reading))) {
			        reading = getMaxReading(getSize(reading));
			      } else {
			        reading--;
			      }
			    } while (!isAscending(reading));
			    return reading;
			  }
			    
}
