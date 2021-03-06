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
package com.agiletec.plugins.jpcontentrefs.aps.system.services.contentrelations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;

/**
 * @author E.Santoboni
 */
public abstract class AbstractContentRefManager extends AbstractService implements IContentRefManager {
	
	@Override
	public void init() throws Exception {
		this.loadContentTypeElement();
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": initialized");
	}
	
	private void loadContentTypeElement() throws ApsSystemException {
		Logger log = ApsSystemUtils.getLogger();
		try {
			String xml = this.getConfigInterface().getConfigItem(this.getConfigItemName());
			if (xml == null) {
				throw new ApsSystemException("Item configurazione assente: " + this.getConfigItemName());
			} else {
				log.finest(this.getConfigItemName()+": " + xml);
				AbstractContentRelactionDOM catDom = this.getConfigDom(xml);
				this.setContentTypeElements(catDom.getContentTypes());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadContentTypeCategory");
			throw new ApsSystemException("Errore in fase di inizializzazione", t);
		}
	}
	
	public abstract String getConfigItemName();
	
	public abstract AbstractContentRelactionDOM getConfigDom() throws ApsSystemException;
	
	public abstract AbstractContentRelactionDOM getConfigDom(String xml) throws ApsSystemException;
	
	@Override
	public void addRelation(String elementCode, String contentType) throws ApsSystemException {
		try {
			SmallContentType smallContentType = 
				(SmallContentType) this.getContentManager().getSmallContentTypesMap().get(contentType);
			if (null == smallContentType) {
				throw new ApsSystemException("TIPO Contenuto inesistente : " + contentType);
			}
			if (null == this.getContentTypeElements().get(contentType)) {
				this.getContentTypeElements().put(contentType, new ArrayList<String>());
			}
			this.checkReference(elementCode);
			List<String> contentTypeCategories = this.getContentTypeElements().get(contentType);
			if (!contentTypeCategories.contains(elementCode)) {
				contentTypeCategories.add(elementCode);
			}
			
			this.updateConfig();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addCategoryRelation");
			throw new ApsSystemException("Errore in addCategoryRelation", t);
		}
	}
	
	protected abstract void checkReference(String elementCode);
	
	@Override
	public void removeRelation(String categoryCode, String contentType) throws ApsSystemException {
		try {
			List<String> contentTypeElements = this.getContentTypeElements().get(contentType);
			contentTypeElements.remove(categoryCode);
			this.updateConfig();
		} catch (ApsSystemException e) {
			ApsSystemUtils.logThrowable(e, this, "removeRelation");
			throw e;
		}
	}
	
	private void updateConfig() throws ApsSystemException {
		try {
			AbstractContentRelactionDOM dom = this.getConfigDom();
			dom.setContent(this.getContentTypeElements());
			String xml = dom.getXMLDocument();
			this.getConfigInterface().updateConfigItem(this.getConfigItemName(), xml);
		} catch (Throwable t) {
			throw new ApsSystemException("Errore in aggiornamento item " + this.getConfigItemName(), t);
		}
	}
	
	protected ConfigInterface getConfigInterface() {
		return _configInterface;
	}
	public void setConfigInterface(ConfigInterface configInterface) {
		this._configInterface = configInterface;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected Map<String, List<String>> getContentTypeElements() {
		return _contentTypeElements;
	}
	protected void setContentTypeElements(Map<String, List<String>> contentTypeElements) {
		this._contentTypeElements = contentTypeElements;
	}
	
	private Map<String, List<String>> _contentTypeElements;
	
	private ConfigInterface _configInterface;
	private IContentManager _contentManager;
	
}
