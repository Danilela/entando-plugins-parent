/*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
* This file is part of jAPS software.
* jAPS is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpcalendar.aps.system.services.calendar;

import java.util.Calendar;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util.EventsOfDayDataBean;

public interface ICalendarManager {

	/**
	 * Restituisce un'array di interi caratterizzante 
	 * abilitati all'utente specificato.
	 * @param requiredMonth Il mese richiesto.
	 * @param user L'utente richiedente.
	 * @return
	 * @throws ApsSystemException 
	 */
	public int[] getEventsForMonth(Calendar requiredMonth, UserDetails user) throws ApsSystemException;
	
	/**
	 * Carica una lista di identificativi di contenuto in base ai valori 
	 * dei campi del bean specificato.
	 * Metodo riservato al EventsOfDayTag.
	 * @param bean Il bean Contenente i dati da erogare.
	 * @return La lista di identificativi di contenuto cercata. 
	 * @throws ApsSystemException
	 */
	public List loadEventsOfDayId(EventsOfDayDataBean bean) throws ApsSystemException;

	/**
	 * Restituisce il primo anno a partire dal quale ci sono eventi.
	 * @return Il primo anno.
	 */
	public int getFirstYear();

	public String getManagedContentType();

	public String getManagedDateStartAttribute();

	public String getManagedDateEndAttribute();

	public static final String REQUIRED_DATE_PATTERN = "yyyyMMdd";

}