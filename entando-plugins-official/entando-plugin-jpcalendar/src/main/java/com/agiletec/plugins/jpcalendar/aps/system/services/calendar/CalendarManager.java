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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedObserver;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util.EventsOfDayDataBean;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util.SmallEventOfDay;

/**
 * Service for calendar data.
 * Transformed to plugin jAPS2.0 by R.Bonazzo
 * 
 * @author E.Santoboni
 */
@SuppressWarnings("serial")
public class CalendarManager extends AbstractService implements PublicContentChangedObserver, ICalendarManager {
	
	@Override
	public void init() throws ApsSystemException {
		this.loadConfig();
		this.initFirstYear();
		ApsSystemUtils.getLogger().config(this.getName() + ": initialized");
	}
	
	@Override
	public int[] getEventsForMonth(Calendar requiredMonth, UserDetails user)
			throws ApsSystemException {
		Logger log = ApsSystemUtils.getLogger();
		if (log.isLoggable(Level.FINEST)) {
			log.info("Required calendar for month "
					+ DateConverter.getFormattedDate(requiredMonth.getTime(),
							"MMMM yyyy"));
		}
		int[] eventsForMonth = new int[31];
		List<Group> groups = this.getAuthorizationManager().getGroupsOfUser(user);
		Set<String> groupForSearch = this.getGroupsForSearch(groups);
		Iterator<String> iter = groupForSearch.iterator();
		while (iter.hasNext()) {
			String groupName = (String) iter.next();
			if (log.isLoggable(Level.FINEST)) {
				log.info("User " + user.getUsername() + " of group "
						+ groupName);
			}
			int[] eventsForMonthOfGroup = this.searchEventsForMonth(
					requiredMonth, groupName);
			for (int i = 0; i < eventsForMonthOfGroup.length; i++) {
				eventsForMonth[i] = eventsForMonth[i]
						+ eventsForMonthOfGroup[i];
			}
		}
		return eventsForMonth;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.agiletec.plugins.calendar.aps.services.calendar.ICalendarManager#
	 * updateFromPublicContentChanged
	 * (com.agiletec.aps.cms.content.event.PublicContentChangedEvent)
	 */
	public void updateFromPublicContentChanged(PublicContentChangedEvent event) {
		Content content = (Content) event.getContent();
		if (content.getTypeCode().equals(_managedContentType)) {
			this._map = new HashMap();
		}
	}
	
	/**
	 * Carica una lista di identificativi di contenuto in base ai valori dei
	 * campi del bean specificato. Metodo riservato al EventsOfDayTag.
	 * 
	 * @param bean Il bean Contenente i dati da erogare.
	 * @return La lista di identificativi di contenuto cercata.
	 * @throws ApsSystemException
	 */
	public List<String> loadEventsOfDayId(EventsOfDayDataBean bean) throws ApsSystemException {
		List<SmallEventOfDay> smallContents = null;
		try {
			smallContents = this.getEventsOfDayTagDAO().loadSmallEventsOfDay(bean);
		} catch (ApsSystemException e) {
			throw e;
		}
		List<String> idList = null;
		if (null != smallContents) {
			idList = this.getRightContentsId(smallContents, bean);
		}
		return idList;
	}

	/**
	 * Cerca l'array di interi relativo agli eventi per mese del gruppo
	 * specificato.
	 * @param requiredMonth Il mese richiesto.
	 * @param groupName Il nome del gruppo.
	 * @return
	 * @throws ApsSystemException
	 */
	private int[] searchEventsForMonth(Calendar requiredMonth, String groupName)
			throws ApsSystemException {
		Map calendarsForGroup = (Map) this._map.get(groupName);
		if (calendarsForGroup == null) {
			calendarsForGroup = new HashMap();
			this._map.put(groupName, calendarsForGroup);
		}
		String eventsForMonthOfGroupKey = DateConverter.getFormattedDate(
				requiredMonth.getTime(), MONTH_FORMAT_KEY);
		int[] eventsForMonthOfGroup = (int[]) calendarsForGroup
				.get(eventsForMonthOfGroupKey);
		if (eventsForMonthOfGroup == null) {
			eventsForMonthOfGroup = this.loadEventsForMonthOfGroup(
					requiredMonth, groupName);
			calendarsForGroup.put(eventsForMonthOfGroupKey,
					eventsForMonthOfGroup);
		}
		return eventsForMonthOfGroup;
	}

	private Set<String> getGroupsForSearch(List<Group> allowedGroups) {
		Set<String> groupForSearch = new HashSet<String>();
		groupForSearch.add(Group.FREE_GROUP_NAME);
		Iterator<Group> groupsIter = allowedGroups.iterator();
		while (groupsIter.hasNext()) {
			Group group = (Group) groupsIter.next();
			if (group.getName().equals(Group.ADMINS_GROUP_NAME)) {
				groupForSearch
						.addAll(getGroupManager().getGroupsMap().keySet());
				break;
			} else {
				groupForSearch.add(group.getName());
			}
		}
		return groupForSearch;
	}

	private int[] loadEventsForMonthOfGroup(Calendar requiredMonth, String groupName) throws ApsSystemException {
		int[] eventsForMonth = null;
		try {
			eventsForMonth = this.getCalendarDao().loadCalendar(requiredMonth,
					groupName, _managedContentType, _managedDateStartAttribute,
					_managedDateEndAttribute);
		} catch (Throwable t) {
			throw new ApsSystemException("Error loading calendar ", t);
		}
		return eventsForMonth;
	}


	private void loadConfig() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem("jpcalendar_Config");
			if (xml == null) {
				throw new ApsSystemException("Configuration Item not present: calendarConfig");
			}
			ApsSystemUtils.getLogger().finest("calendarConfig: " + xml);
			CalendarConfigDOM dom = new CalendarConfigDOM(xml);
			this._managedContentType = dom.getManageContentType();
			this._managedDateStartAttribute = dom.getManageStartAttribute();
			this._managedDateEndAttribute = dom.getManageEndAttribute();
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().throwing(this.getName(), "loadConfig", t);
			throw new ApsSystemException("Error on initialization", t);
		}
	}

	private void initFirstYear() throws ApsSystemException {
		try {
			this._firstYear = this.getCalendarDao().getFirstYear(_managedContentType, _managedDateStartAttribute);
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().throwing(this.getName(), "initFirstYear", t);
			throw new ApsSystemException("Error on initialization", t);
		}
	}
	
	private List<String> getRightContentsId(List<SmallEventOfDay> smallContents, EventsOfDayDataBean bean) {
		List<String> contentsId = new ArrayList<String>();
		for (int i = 0; i < smallContents.size(); i++) {
			SmallEventOfDay smallContent = smallContents.get(i);
			if (this.isRightContent(smallContent, bean)) {
				contentsId.add(smallContent.getId());
			}
		}
		return contentsId;
	}

	/**
	 * Individua se il contenuto individuato dall'oggetto smallContent è
	 * compatibile con i dati specificati nel bean.
	 * 
	 * @param smallContent
	 * @param bean
	 * @return
	 */
	private boolean isRightContent(SmallEventOfDay smallContent,
			EventsOfDayDataBean bean) {
		boolean isRight = false;
		smallContent.verifyDates();
		if (null != smallContent.getStart()) {
			Calendar requiredDayCal = Calendar.getInstance();
			requiredDayCal.setTime(bean.getRequiredDay());
			requiredDayCal.set(Calendar.HOUR, 2);
			Date requiredDay = requiredDayCal.getTime();
			Date start = smallContent.getStart();
			Date end = smallContent.getEnd();
			isRight = null != start && null != end
					&& (start.before(requiredDay) && end.after(requiredDay));
		}
		return isRight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.agiletec.plugins.calendar.aps.services.calendar.ICalendarManager#
	 * getFirstYear()
	 */
	public int getFirstYear() {
		return this._firstYear;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.agiletec.plugins.calendar.aps.services.calendar.ICalendarManager#
	 * getManagedContentType()
	 */
	public String getManagedContentType() {
		return _managedContentType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.agiletec.plugins.calendar.aps.services.calendar.ICalendarManager#
	 * getManagedDateStartAttribute()
	 */
	public String getManagedDateStartAttribute() {
		return _managedDateStartAttribute;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.agiletec.plugins.calendar.aps.services.calendar.ICalendarManager#
	 * getManagedDateEndAttribute()
	 */
	public String getManagedDateEndAttribute() {
		return _managedDateEndAttribute;
	}
	
	protected ICalendarDAO getCalendarDao() {
		return _calendarDAO;
	}
	public void setCalendarDAO(ICalendarDAO calendarDAO) {
		this._calendarDAO = calendarDAO;
	}
	
	public void setEventsOfDayTagDAO(IEventsOfDayTagDAO eventsOfDayTagDAO) {
		this._eventsOfDayTagDAO = eventsOfDayTagDAO;
	}
	protected IEventsOfDayTagDAO getEventsOfDayTagDAO() {
		return _eventsOfDayTagDAO;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	
	private int _firstYear;
	private String _managedContentType;
	private String _managedDateStartAttribute;
	private String _managedDateEndAttribute;
	private final String MONTH_FORMAT_KEY = "yyyy-MM";

	/**
	 * Mappa (indicizzata in base al nome del gruppo) delle mappe (indicizzate
	 * in base al mese richiesto nel formato MONTH_FORMAT_KEY) degli array di
	 * interi caratterizzanti il numero di eventi per giorno abilitati ad un
	 * gruppo.
	 */
	private Map _map = new HashMap();
	
	private ICalendarDAO _calendarDAO;
	private IEventsOfDayTagDAO _eventsOfDayTagDAO;
	private ConfigInterface _configManager;
	private IAuthorizationManager _authorizationManager;
	private IGroupManager _groupManager;

}
