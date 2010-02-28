<#-- ******************************* -->
<#-- Make the HTML for group listing -->
<#-- ******************************* -->
<#macro listGroups parent>
	<#assign total = parent.size() - 1>
	
	<#if (total >= 0 )>
		<#list 0..total as i>
			<#global level = level + 2>
			<#assign node = parent.getNode(i)>
			
			<tr bgcolor="#f4f4f4">
			<td><span class="gen"><#list 0..level as j>&nbsp;</#list>${node.name}</span></td>
			<td align="center"><span class="gen"><a href="${contextPath}/${moduleName}/edit/${node.id}${extension}">${I18n.getMessage("Groups.List.Edit")}</a></span></td>
			<td align="center"><input type="checkbox" name="group_id" value="${node.id}"/></td>
			<td class="row2" align="center"><span class="gen"><a href="${contextPath}/${moduleName}/permissions/${node.id}${extension}">${I18n.getMessage("Permissions")}</a></span></td>
			</tr>

			<@listGroups node/>

		<#global level = level - 2>
		</#list>
	</#if>
</#macro>

<#-- ******************************************** -->
<#-- Create a <select> HTML field with the groups -->
<#-- ******************************************** -->
<#macro selectFieldGroups name groups parentId multiple selectedList>
	<select name="${name}" <#if multiple>multiple="multiple" size="4"</#if>>
	
	<#if !multiple>
		<option value="0">${I18n.getMessage("topLevelGroup")}</option>
	</#if>
		
	<#assign len = groups.size() - 1>
	<#if (len > -1)>
	<#list 0 .. len as i>
		<#assign node = groups.get(i)>

		<#if !group?exists || node.id != group.id>
			<#global level = 0>
			<option value="${node.id}"<#if parentId == node.id || selectedList.contains(node.id)> selected="selected"</#if>>${node.name}</option>
			<@selectOption node, parentId/>
		</#if>
	</#list>
	</#if>

	</select>	
</#macro>

<#-- *************************************************************** -->
<#-- Create the <option> fields related to "selectFieldGroups" macro -->
<#-- *************************************************************** -->
<#macro selectOption node parentId>
	<#assign len = node.size() - 1>
	
	<#if (len >= 0)>
		<#list 0..len as i>
			<#global level = level + 2>
			<#assign n = node.getNode(i)>

			<#if !group?exists || n.id != group.id>
				<option value="${n.id}"<#if parentId == n.id || selectedList.contains(n.id)> selected="selected"</#if>><#list 0..level as j>&nbsp;</#list>${n.name}</option>
				<@selectOption n, parentId/>
				
				<#global level = level - 2>
			</#if>
		</#list>
	</#if>
</#macro>
