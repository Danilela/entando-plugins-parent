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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.workflow;

import java.util.List;

import com.agiletec.aps.system.services.role.Role;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;

/**
 * @author E.Santoboni
 */
public class WorkflowListAction extends AbstractWorkflowAction implements IWorkflowListAction {
	
	@Override
	public List<SmallContentType> getContentTypes() {
		return this.getContentManager().getSmallContentTypes();
	}
	
	@Override
	public Role getRole(String typeCode) {
		Role role = null;
		String roleName = this.getWorkflowManager().getRole(typeCode);
		if (roleName!=null) {
			role = this.getRoleManager().getRole(roleName);
		}
		return role;
	}
	
}