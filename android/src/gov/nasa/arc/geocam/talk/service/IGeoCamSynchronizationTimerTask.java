// __BEGIN_LICENSE__
// Copyright (C) 2008-2010 United States Government as represented by
// the Administrator of the National Aeronautics and Space Administration.
// All Rights Reserved.
// __END_LICENSE__

package gov.nasa.arc.geocam.talk.service;

// TODO: Auto-generated Javadoc
/**
 * The Interface IGeoCamSynchronizationTimerTask.
 */
public interface IGeoCamSynchronizationTimerTask extends Runnable {
	
	/**
	 * Reset timer.
	 */
	void resetTimer();

	/**
	 * Stop timer.
	 */
	void stopTimer();
}
