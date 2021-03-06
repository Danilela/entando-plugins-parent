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
package com.agiletec.plugins.jpcmstagcloud.aps.tags;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcmstagcloud.aps.system.JpcmstagcloudSystemConstants;
import com.agiletec.plugins.jpcmstagcloud.aps.system.services.tagcloud.ITagCloudManager;

/**
 * @author E.Santoboni
 */
public class ContentListTag extends TagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		ITagCloudManager tagCloudManager = (ITagCloudManager) ApsWebApplicationUtils.getBean(JpcmstagcloudSystemConstants.TAG_CLOUD_MANAGER, this.pageContext); 
		ServletRequest request =  this.pageContext.getRequest();
		try {
			UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			String tagCategoryCode = request.getParameter("tagCategoryCode");
			List<String> contentsId = tagCloudManager.loadPublicTaggedContentsId(tagCategoryCode, currentUser);
			this.pageContext.setAttribute(this.getListName(), contentsId);
			request.setAttribute("tagCategoryCode", tagCategoryCode);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Errore tag", t);
		}
		return super.doStartTag();
	}
	
	@Override
	public void release() {
		super.release();
		this.setListName(null);
	}
	
	/**
	 * Restituisce il nome con il quale viene inserita nel pageContext
	 * la lista degli identificativi trovati.
	 * @return Returns the listName.
	 */
	public String getListName() {
		return _listName;
	}
	
	/**
	 * Setta il nome con il quale viene inserita nel pageContext
	 * la lista degli identificativi trovati.
	 * @param listName The listName to set.
	 */
	public void setListName(String listName) {
		this._listName = listName;
	}
	
	private String _listName;
	
}
